# Roadmap

Brings the repository from the state recorded in [`audit-2026-10.md`](audit-2026-10.md) to two published, convention-aligned libraries shared by several projects. The logger ships first; the tracker and the integrations follow.

## Decisions

| Topic        | Decision                                                                                      | Reference                                                      |
|--------------|-----------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| Distribution | Maven Central, `groupId` `io.github.cyrillrx`, one version for every module, published on tag | [ADR-001](adr/adr-001-distribution-via-maven-central.md)       |
| Toolchain    | Latest stable Kotlin, AGP, Gradle and JDK LTS; consumers align                                | [ADR-001](adr/adr-001-distribution-via-maven-central.md)       |
| API model    | Injectable instances plus an optional global facade, thread-safe                              | [ADR-002](adr/adr-002-api-instances-with-global-facade.md)     |
| Privacy      | Third-party children opt in per consent category; redaction before any send                   | [ADR-003](adr/adr-003-consent-and-redaction.md)                |
| Backends     | Sentry and Firebase Analytics; legacy modules removed; nested modules; CMP sample             | [ADR-004](adr/adr-004-supported-backends-and-module-layout.md) |
| Logger       | Kept in-house and modernised rather than replaced by Kermit                                   | [ADR-002](adr/adr-002-api-instances-with-global-facade.md)     |

## Phase 0 — Hygiene and documents (~1 day)

- [x] Restore the staged `shared/logger/build.gradle.kts`.
- [x] Align the AI tooling with the consumers: `.claude/CLAUDE.md`, `.claude/settings.json`, `AGENTS.md`, `docs/conventions/`.
- [x] Write the audit, this roadmap and ADR-001 to ADR-004.
- [x] Open [cyrillrx/coding-conventions#42](https://github.com/cyrillrx/coding-conventions/issues/42) for a project audit skill.

## Phase 1 — Build modernisation (~2-3 days)

- [ ] Latest stable Kotlin, AGP, Gradle, ktlint plugin and Kover; latest JDK LTS supported by every target; latest compileSdk.
- [ ] `com.android.kotlin.multiplatform.library` instead of `com.android.library`.
- [ ] Targets: Android, `iosArm64`, `iosSimulatorArm64`, `jvm()`. Drop `iosX64` and the `desktop` name.
- [ ] Convention plugins in `buildSrc`: `kmp-library` (targets, ktlint, Kover, `explicitApi()`, ABI validation, Dokka) and `published-library` (vanniktech, POM, signing, explicit `artifactId`).
- [ ] `.editorconfig` copied from `coding-conventions/configs/kotlin/`.
- [ ] `git mv shared/logger logger/core` and `git mv shared/tracker tracker/core`.
- [ ] Remove `logger-crashlytics`, `tracker-amplitude`, `tracker-segment`, `device`, `notifier`, `androidApp`, `iosApp` and the dead catalog entries.
- [ ] `CHANGELOG.md` in Keep a Changelog format replaces the `release_notes.txt` files.
- [ ] Dependabot: drop `swift`, add `github-actions`.

## Phase 2 — Logger 1.0 (~3-4 days)

- [ ] `Severity` in natural order: `VERBOSE < DEBUG < INFO < WARN < ERROR < FATAL`.
- [ ] Immutable `LogEntry(severity, tag, message, throwable, attributes, timestamp)` using `kotlin.time`.
- [ ] `LogChild` with `isLoggable(severity, tag)` and `log(entry)`.
- [ ] `Logger` interface with a lazy `message: () -> String`, structured attributes, and `verbose` … `fatal` extensions.
- [ ] `CompositeLogger`: atomically swapped immutable child list, per-child failure isolation routed to `onChildError`.
- [ ] `object Log` global facade with `Log.install(logger)`; the `L` typealias goes.
- [ ] Console children: `LogcatChild`, `OsLogChild` (subsystem and category), `ConsoleChild` (JVM, fixes the double print, shares the JVM stack walk).
- [ ] `RamLogChild` in the main source set; `commonTest` coverage of filtering, laziness, failure isolation, concurrent registration and formatting.
- [ ] KDoc on every public declaration.

## Phase 3 — Publication and CI, release 1.0.0 (~1.5-2 days)

- [ ] Manual prerequisites: Central Portal account, `io.github.cyrillrx` namespace verified, GPG key published, repository secrets set.
- [ ] `ci.yml`: ktlint, JVM tests, ABI check and Kover on Linux; iOS simulator tests on macOS.
- [ ] `release.yml`: publish to Maven Central on `v*` tags and create the GitHub Release from `CHANGELOG.md`.
- [ ] Dry run with `v1.0.0-rc1`, then release `io.github.cyrillrx:logger:1.0.0`.

## Phase 4 — Tracker (~3-4 days, released in 1.1.0)

- [ ] `Tracker` interface, `CompositeTracker` and a global facade, sharing the logger's registry and failure isolation.
- [ ] Immutable `TrackEvent` with typed `AttributeValue`s and a builder DSL.
- [ ] Immutable `TrackerContext` snapshotted per event.
- [ ] `ConsentCategory` (`ANALYTICS`, `CRASH_REPORTING`), denied by default.
- [ ] `Redactor` pipeline applied before third-party children, with a sensitive-keys redactor provided.
- [ ] `LogEvent` removed; the logger-to-tracker bridge becomes a breadcrumb child in `:logger:sentry`.
- [ ] `RamTracker` replaces `InMemoryTracker` and `FakeUser`; tests isolated and extended.

## Phase 5 — `:logger:sentry` (~2-3 days)

- [ ] Spike: linking Sentry Cocoa through SPM in an Xcode project that embeds a Kotlin framework.
- [ ] `SentryLogChild`: `ERROR` and `FATAL` as events, lower levels as breadcrumbs.
- [ ] Initialisation helper with `sendDefaultPii = false`, gated by `CRASH_REPORTING` consent.

## Phase 6 — `:tracker:firebase` (~2-3 days)

- [ ] Spike: `dev.gitlive:firebase-analytics` (preferred, matches the consumers' `firebase-kotlin-sdk`) against a native Android implementation with an iOS bridge; record the outcome in ADR-004.
- [ ] Firebase limits enforced by truncation and a logged warning, never by a swallowed exception.
- [ ] Collection disabled until `ANALYTICS` consent is granted.

## Phase 7 — Compose Multiplatform sample (~2 days)

- [ ] `:sample` for Android, iOS and Desktop, consuming the libraries through one umbrella iOS framework.
- [ ] Demo screen: log levels, consent toggles, events.
- [ ] Sample built in CI as an integration smoke test.

## Phase 8 — Conventions and consumers (~3-4 days)

- [ ] PR to `cyrillrx/coding-conventions`: `logging-conventions.md`, library publishing rules, the latest-toolchain policy, nested module naming.
- [ ] Adopt the libraries in each consumer, after aligning its toolchain on the latest stable versions.
