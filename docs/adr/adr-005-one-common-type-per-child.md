# ADR-005: One common type per child, platform specifics behind `expect`

> **Status**: Accepted | **Date**: 2026-10-17 | **Context**: Console children of logger 1.0, and every child that wraps a third-party SDK

## Decision

**One public type per child, in common code.** Every child — the console child and every child that wraps a third-party SDK — is a single public class in `commonMain`. What varies per platform stays `internal`, behind an `expect` declaration with one `actual` per platform. A child whose SDK is already multiplatform, such as Sentry's, declares no `expect` at all.

**The console child.**

```kotlin
public class ConsoleChild(
    minSeverity: Severity = Severity.VERBOSE,
    private val clickableLogs: Boolean = false,
) : SeverityLogChild(minSeverity)

internal expect fun writeToConsole(entry: LogEntry, body: String)
```

- Common code builds the body: the message, then the attributes, then the stack trace — or, when `clickableLogs` is set, a link to the method that logged.
- Each platform writes the body to its native output, which supplies the header:

| Platform | Output                                                                   | Header                                 | Severity                                                                                      |
|----------|--------------------------------------------------------------------------|----------------------------------------|-----------------------------------------------------------------------------------------------|
| Android  | `android.util.Log.println`, split at line breaks under ~4,000 characters | Logcat's time, level and tag columns   | `VERBOSE` to `ERROR` map to V to E; `FATAL` maps to `ASSERT`                                  |
| iOS      | `os_log`: subsystem = app bundle id, category = tag, body as `%s`        | Console.app's time and type columns    | `VERBOSE`, `DEBUG` → debug; `INFO` → info; `WARN` → default; `ERROR` → error; `FATAL` → fault |
| JVM      | `System.out`, and `System.err` from `ERROR` up                           | `timestamp - SEVERITY - tag - ` prefix | Carried by the prefix                                                                         |

- `clickableLogs` has no effect on iOS, where no stack frame can be turned into a link.
- `FATAL` never goes through `Log.wtf`, which can terminate the process depending on the device's configuration.
- One `os_log_t` is created per tag and cached.
- `LogCat`, `NsLogger`, `OsLogger` and `SystemOutLog` are removed.

## Context

The library shipped four console children, each public and tied to a platform: `LogCat` in `androidMain`, `NsLogger` and `OsLogger` in `iosMain`, `SystemOutLog` in `commonMain` ([audit](../audit-2026-10.md)). A consumer had to pick one per platform in platform code, while `Log.install` ([ADR-002](adr-002-api-instances-with-global-facade.md)) is called from common code.

Each child copied the same message-building branch, and the copies drifted: `SystemOutLog` printed every entry twice, `OsLogger` logged to `OS_LOG_DEFAULT` with no subsystem or category, and none of them prints the attributes `LogEntry` gained in logger 1.0.

The platform outputs bring their own constraints:

1. Logcat truncates a message beyond about 4,000 characters, which cuts long stack traces.
2. `os_log` shows a dynamic `%s` argument as `<private>` outside a debugger session.
3. `expect class` is still Beta and requires the `-Xexpect-actual-classes` compiler flag.

## Rationale

### Why one common type per child

Consumers configure logging in common code. A child that exists only in platform code forces every consumer to write its own `expect`/`actual` to reach it. One common type gives one KDoc, one set of behaviour tests in `commonTest`, and one place where the body is built.

### Why an internal `expect fun` rather than an `expect class`

Only the output differs per platform; filtering, formatting and links do not. Expecting the output alone keeps everything else shared and tested once, and avoids the Beta flag.

### Why native outputs

Logcat and `os_log` filter by level and tag in the tools developers already use, and keep entries in the system log. `println` on Android lands in Logcat under the `System.out` tag at the info level, losing both.

### Why keep `os_log`'s private default

[ADR-003](adr-003-consent-and-redaction.md) leaves local children unredacted because their data stays on the device. `os_log` entries can leave it, in a sysdiagnose or through Console.app on a paired Mac. `%s` keeps them `<private>` there, while a debugger session still shows them in clear.

### Why errors go to the standard error on the JVM

Servers, CI runners and IDE consoles tell the two streams apart, so errors can be filtered or highlighted. The relative order of the two streams is lost only by a reader that merges them.

## Consequences

- `ConsoleChild` is the only console child. Consumers replace `LogCat`, `NsLogger`, `OsLogger` and `SystemOutLog` with it, in common code.
- A `jvmAndAndroid` source set shares the stack walk that builds the links.
- The emoji prefix of `OsLogger` goes away: the `os_log` type carries the severity.
- `:logger:sentry` is a common class with no `expect`, since the Sentry SDK is multiplatform.
- Whether `:tracker:firebase` needs an `expect` is settled by the spike [ADR-004](adr-004-supported-backends-and-module-layout.md) plans; either way its public type is common.

## Alternatives considered

**`expect class ConsoleChild`** — Rejected: requires the Beta `-Xexpect-actual-classes` flag, and each `actual` repeats the filtering and formatting.

**An `expect fun consoleChild()` factory plus public platform classes** — Rejected: keeps one public type per platform, whose copies drift as the four legacy children did.

**`println` on every platform** — Rejected: loses Logcat's levels and `os_log`'s types, and the filtering both tools offer.

**The same `timestamp - SEVERITY - tag` line on every platform** — Rejected: repeats the columns Logcat and Console.app already show.

**`%{public}s` for `os_log`** — Rejected: makes every log readable in a sysdiagnose taken from a production device.

**An `expect` for every SDK child, multiplatform SDKs included** — Rejected: adds `actual` declarations that all do the same thing.
