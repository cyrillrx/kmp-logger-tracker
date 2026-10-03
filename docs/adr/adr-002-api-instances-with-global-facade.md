# ADR-002: Injectable instances with an optional global facade

> **Status**: Accepted | **Date**: 2026-10-03 | **Context**: Logger 1.0 redesign, before implementation; applies to the tracker in 1.1.0

## Decision

The logger stays an in-house library. Its API, and the tracker's after it, is rebuilt around **interfaces that can be instantiated and injected**, with a **global facade** for call sites that do not want injection.

```kotlin
public enum class Severity { VERBOSE, DEBUG, INFO, WARN, ERROR, FATAL }

public data class LogEntry(
    val severity: Severity,
    val tag: String,
    val message: String,
    val throwable: Throwable?,
    val attributes: Map<String, String>,
    val timestamp: Instant,
)

public interface LogChild {
    public fun isLoggable(severity: Severity, tag: String): Boolean = true
    public fun log(entry: LogEntry)
}

public interface Logger {
    public fun isLoggable(severity: Severity, tag: String): Boolean
    public fun log(
        severity: Severity,
        tag: String,
        throwable: Throwable? = null,
        attributes: Map<String, String> = emptyMap(),
        message: () -> String,
    )
}

public class CompositeLogger(
    children: List<LogChild> = emptyList(),
    private val onChildError: (LogChild, Throwable) -> Unit = { _, _ -> },
) : Logger {
    public fun add(child: LogChild)
    public fun remove(child: LogChild)
}

public object Log : Logger { public fun install(logger: Logger) }
```

- `verbose`, `debug`, `info`, `warn`, `error` and `fatal` are extensions on `Logger`, so every implementation gets them.
- `Severity` is in natural order: a higher ordinal is more severe, and filters read `severity >= minSeverity`.
- `message` is a lambda. `CompositeLogger` evaluates it once, and only when at least one child accepts the entry.
- **Thread safety**: `CompositeLogger` keeps its children in an immutable list held by an `kotlinx-atomicfu` atomic reference. `add` and `remove` swap the list; `log` iterates a snapshot. Registering a child from any thread, or from inside a child, is safe.
- **Failure isolation**: an exception thrown by a child is caught per child and handed to `onChildError`. It never reaches the caller, and the other children still receive the entry.
- `Log` delegates to a default `CompositeLogger` until `install` replaces it. The `L` typealias is removed.
- In-memory children for tests are named `RamLogChild`, and `RamTracker` for the tracker, and live in the main source set so consumers can use them.

The tracker follows the same shape in 1.1.0: a `Tracker` interface, a `CompositeTracker` sharing the registry and isolation mechanism, an immutable `TrackEvent` with typed attribute values, and an immutable `TrackerContext` captured per event. The name of its global facade is settled when the tracker is implemented.

## Context

The libraries were two global `object`s, `Logger` and `Tracker`, holding mutable `HashSet` and `HashMap` registries without synchronisation ([audit](../audit-2026-10.md)). Consequences:

1. Registering a child while another thread logs throws `ConcurrentModificationException` to the caller on the JVM, and is a data race on Kotlin/Native.
2. Tests mutate shared global state and depend on their execution order.
3. A consumer cannot inject a logger into a class, nor replace it in a test.
4. Messages are built eagerly, even when no child will log them.
5. The two libraries diverged: `release` against `clear`, interface against lambda for errors, protected `shouldLog` against public `shouldTrack`.

No consumer depends on the libraries yet, so the API can break freely.

The alternative to a redesign was to adopt Kermit, the KMP logging library most used in 2026.

## Rationale

### Why keep an in-house logger

Kermit would replace the console children but not the tracker, the consent model or the redaction pipeline ([ADR-003](adr-003-consent-and-redaction.md)), which have to be built anyway. Keeping the logger in-house lets both libraries share one child registry, one failure-isolation mechanism and one privacy model. The logger is small; its cost is the console children, which are already written.

### Why instances and a facade

Instances make dependencies explicit and testable — the convention favours `Ram*` doubles over mocks, which requires something to inject. A facade keeps one-line logging available where injection is not worth it, such as a top-level function or an `ImportCoercion` helper. Kermit (`Logger` instances plus `Logger` companion) and Timber (`Timber.plant`) settle on the same split.

### Why an immutable snapshot rather than a lock

Logging happens far more often than registration. A copy-on-write list makes the hot path lock-free and allocation-free, works identically on the JVM and Kotlin/Native, and tolerates re-entrant registration from inside a child.

### Why a lambda message

Building a debug message costs string formatting on every call even when debug logs are filtered out. The lambda is the convention of Kermit, Timber's Kotlin extensions and kotlin-logging.

## Consequences

- Every existing call site breaks: `Logger.info("TAG", "msg")` becomes `Log.info("TAG") { "msg" }`. Only the removed demo apps call the libraries today.
- The Obj-C names `KMPLogger` and `KMPTracker` go away; Swift callers go through the consumer's umbrella framework.
- `kotlinx-atomicfu` becomes a dependency of `:logger:core`.
- `explicitApi()` and the ABI dump make each public declaration a deliberate choice.
- The tracker's lifecycle callbacks (`onUserUpdated`, `onConnectivityChanged`, `onTrackerRemoved`) are reviewed when the tracker is rebuilt, so the two registries share one contract.

## Alternatives considered

**Adopt Kermit directly** — Rejected: covers logging only; consent, redaction and the tracker would still be in-house, with two registry models side by side.

**Facade over Kermit** — Rejected: adds a third-party dependency for console children that already exist, and still leaves two registry models.

**Keep global singletons, made thread-safe** — Rejected: fixes the races but leaves the library untestable without global state.

**Instances only, no facade** — Rejected: forces injection into every call site, including pure functions where a logger parameter is noise.

**Lock around the registry** — Rejected: a lock on every log call for an event that happens a handful of times per process; `Mutex` is not reentrant, so a child registering another child would deadlock.
