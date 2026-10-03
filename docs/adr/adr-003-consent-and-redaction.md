# ADR-003: Consent per category and redaction before any send

> **Status**: Accepted | **Date**: 2026-10-03 | **Context**: Tracker and integrations redesign, before implementation

## Decision

Privacy is enforced by the libraries, not left to each consumer.

**Consent per category.**

```kotlin
public enum class ConsentCategory { ANALYTICS, CRASH_REPORTING }
```

- A child that sends data off the device declares its category: `:tracker:firebase` declares `ANALYTICS`, `:logger:sentry` declares `CRASH_REPORTING`.
- A child that keeps data on the device — Logcat, `os_log`, the JVM console, the `Ram*` doubles — declares no category and is always active.
- **Every category is denied by default.** A categorised child receives nothing until the app grants its category with `setConsent(category, granted = true)`. Revoking consent stops delivery immediately.
- Consent state is held in memory. Persisting the user's choice belongs to the consumer, which re-applies it at start-up.

**Redaction before any send.**

```kotlin
public fun interface Redactor<T> {
    /** Returns the redacted value, or null to drop it. */
    public fun redact(value: T): T?
}
```

- Redactors run, in order, on every `LogEntry` and `TrackEvent` before they reach a categorised child. Local children receive the original, so debugging on the device stays complete.
- A `SensitiveKeysRedactor(keys)` is provided: it masks the value of every listed attribute key, so a consumer can keep a secret — an invitation code, a token — out of every log, event and crash report.

**Privacy-preserving SDK defaults.**

- Sentry is initialised with `sendDefaultPii = false`, and only once `CRASH_REPORTING` is granted.
- Firebase Analytics collection is disabled until `ANALYTICS` is granted.

## Context

Consumers handle personal data, and some of them forbid specific values — invitation codes, for instance — from reaching any log, analytics event or crash report. GDPR requires prior consent for analytics, and the consumers are showcase projects distributed in the EU.

The old tracker had an `applyConsent(hasConsent, tracker)` helper that added or removed a child; nothing prevented a child from sending data before consent, and nothing filtered the data itself. The logger had no notion of consent at all.

The questions were: who enforces consent, at which granularity, and how sensitive values are kept out of third parties.

## Rationale

### Why the library enforces consent

A rule enforced by every consumer is a rule that one consumer will eventually forget. Gating delivery inside `CompositeLogger` and `CompositeTracker` makes "nothing leaves before consent" true by construction, for every project that adopts the libraries.

### Why categories rather than per-child consent

Consent banners ask about purposes — analytics, crash reporting — not vendors. Categories map one-to-one onto the banner, and adding a second analytics vendor later needs no new consent question.

### Why denied by default

GDPR requires opt-in for analytics. A default of "granted" would make the safe behaviour depend on the consumer calling the API early enough.

### Why redaction applies only to categorised children

The risk is data leaving the device. Redacting local console output would make on-device debugging harder for no privacy gain.

## Consequences

- Every categorised child is built on a common base that declares its category; `CompositeLogger` and `CompositeTracker` check the consent state on each dispatch.
- A consumer that forgets to grant consent sees nothing in Sentry or Firebase. The README of each integration states it, and the sample shows the consent toggles.
- Redactors add a pass over each entry sent to a third party; entries filtered out by severity never reach them.
- The library does not persist consent; each consumer stores the user's choice and re-applies it at start-up.

## Alternatives considered

**Consent left to each consumer** — Rejected: relies on every project getting it right, and contradicts the goal of a library that is safe by default.

**One global on/off switch** — Rejected: does not match how consent is asked, and blocks crash reporting when a user refuses analytics.

**Granted by default** — Rejected: incompatible with GDPR opt-in for analytics.

**Redaction left to each SDK's hooks (`beforeSend`)** — Rejected: one hook per vendor, each with its own model; a redactor written once applies to every child.
