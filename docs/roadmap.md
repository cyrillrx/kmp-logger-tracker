# Roadmap

Brings the repository from the state recorded in [`audit-2026-10.md`](audit-2026-10.md) to two published, convention-aligned libraries shared by several projects. The logger ships first; the tracker and the integrations follow.

## Decisions

| Topic        | Decision                                                                                      | Reference                                                      |
|--------------|-----------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| Distribution | Maven Central, `io.github.cyrillrx`, one version, published on tag, a beta before `1.0.0`     | [ADR-001](adr/adr-001-distribution-via-maven-central.md)       |
| Toolchain    | Latest stable Kotlin, AGP, Gradle and JDK LTS; consumers align                                | [ADR-001](adr/adr-001-distribution-via-maven-central.md)       |
| API model    | Injectable instances plus an optional global facade, thread-safe                              | [ADR-002](adr/adr-002-api-instances-with-global-facade.md)     |
| Privacy      | Third-party children opt in per consent category; redaction before any send                   | [ADR-003](adr/adr-003-consent-and-redaction.md)                |
| Backends     | Sentry and Firebase Analytics; legacy modules removed; nested modules; CMP sample             | [ADR-004](adr/adr-004-supported-backends-and-module-layout.md) |
| Logger       | Kept in-house and modernised rather than replaced by Kermit                                   | [ADR-002](adr/adr-002-api-instances-with-global-facade.md)     |

## Phase 0 — Hygiene and documents (~1 day)

- [x] Align the AI tooling with the consumers: `.claude/CLAUDE.md`, `.claude/settings.json`, `AGENTS.md`, `docs/conventions/`.
- [x] Write the audit, this roadmap and ADR-001 to ADR-004.
- [x] Open [cyrillrx/coding-conventions#42](https://github.com/cyrillrx/coding-conventions/issues/42) for a project audit skill.

## Phase 1 — Build modernisation (~2-3 days)

- [x] Latest stable Kotlin, AGP, Gradle and ktlint plugin; JDK 25 for every compilation, Java 25 bytecode on `jvm()` and Java 21 on Android; latest compileSdk.
- [x] `com.android.kotlin.multiplatform.library` instead of `com.android.library`.
- [x] Targets: Android, `iosArm64`, `iosSimulatorArm64`, `jvm()`. Drop `iosX64` and the `desktop` name.
- [x] `kmp-library` convention plugin in `buildSrc`: targets, toolchain, Android host tests, ktlint and Kover.
- [x] `.editorconfig` copied from `coding-conventions/configs/`.
- [x] `git mv shared/logger logger/core`, `git mv shared/tracker tracker/core` and `git mv shared/tracker-firebase tracker/firebase`.
- [x] Remove `logger-crashlytics`, `tracker-amplitude`, `tracker-segment`, `device`, `notifier`, `androidApp`, `iosApp` and the dead catalog entries.
- [x] `CHANGELOG.md` in Keep a Changelog format replaces the `release_notes.txt` files.
- [x] Dependabot: drop `swift`, add `github-actions`.
- [x] `.github/pull_request_template.md` replaced by the shared template from `cyrillrx/coding-conventions`.

## Phase 2 — Logger 1.0 (~3-4 days)

- [x] `Severity` in natural order: `VERBOSE < DEBUG < INFO < WARN < ERROR < FATAL`.
- [x] Immutable `LogEntry(severity, tag, message, throwable, attributes, timestamp)` using `kotlin.time`.
- [x] `LogChild` with `isLoggable(severity, tag)` and `log(entry)`.
- [x] `Logger` interface with a lazy `message: () -> String`, structured attributes, and `verbose` … `fatal` extensions.
- [x] `CompositeLogger`: atomically swapped immutable child list, per-child failure isolation routed to `onChildError`.
- [x] `object Log` global facade with `Log.install(logger)`; the `L` typealias goes.
- [ ] `ConsoleChild` ([ADR-005](adr/adr-005-one-common-type-per-child.md)), one common type writing to the JVM console, Logcat ([#40](https://github.com/cyrillrx/kmp-logger-tracker/issues/40)) and `os_log` ([#41](https://github.com/cyrillrx/kmp-logger-tracker/issues/41)); it replaces `SystemOutLog`, `LogCat`, `NsLogger` and `OsLogger`.
- [ ] A per-thread `onChildError` recursion guard ([#33](https://github.com/cyrillrx/kmp-logger-tracker/issues/33)) and a guard against re-entrant logging ([#37](https://github.com/cyrillrx/kmp-logger-tracker/issues/37)).
- [x] `RamLogChild` in the main source set; `commonTest` coverage of filtering, laziness, failure isolation and formatting; JVM coverage of concurrent registration.
- [ ] KDoc on every public declaration ([#46](https://github.com/cyrillrx/kmp-logger-tracker/issues/46)).
- [x] `LogHelper` internal ([ADR-005](adr/adr-005-one-common-type-per-child.md)).
- [x] `explicitApi()` and ABI validation in `kmp-library`; the legacy tracker opts out of `explicitApi()` until phase 4.

## Phase 3 — Publication and CI, release 1.0.0 (~1.5-2 days)

- [ ] Manual prerequisites: Central Portal account, `io.github.cyrillrx` namespace verified, GPG key published, repository secrets set.
- [ ] `ci.yml`: ktlint, JVM tests, ABI check and Kover on Linux; iOS simulator tests on macOS.
- [x] `published-library` convention plugin in `buildSrc`: vanniktech, POM, signing, explicit `artifactId`, Dokka.
- [ ] `release.yml`: publish to Maven Central on `v*` tags and create the GitHub Release from `CHANGELOG.md`, as a prerelease for `-rcN` tags.
- [ ] A single `VERSION_NAME` in `gradle.properties`, replacing `LOGGER_VERSION` and `TRACKER_VERSION` in `Version.kt` and checked against the tag by `release.yml`; `published-library` applied to `:logger:core` only.
- [ ] Inspect `publishToMavenLocal`, then release `v1.0.0-rc1` as the beta, adopted by a first consumer after aligning its toolchain.
- [ ] Release `io.github.cyrillrx:logger:1.0.0` once its last release candidate is adopted with no open regression.

## Phase 4 — Tracker (~3-4 days, released in 1.1.0)

- [ ] `Tracker` interface, `CompositeTracker` and a global facade, sharing the logger's registry and failure isolation.
- [ ] Immutable `TrackEvent` with typed `AttributeValue`s and a builder DSL.
- [ ] Immutable `TrackerContext` snapshotted per event.
- [ ] `ConsentCategory` (`ANALYTICS`, `CRASH_REPORTING`), denied by default.
- [ ] `Redactor` pipeline applied before third-party children, with a sensitive-keys redactor provided.
- [ ] Pattern-based redactor (emails, code formats) applied to messages and throwables too, since key-based redaction misses values written in free text; amend ADR-003 accordingly.
- [x] `LogEvent` removed, and with it the tracker's dependency on the logger.
- [ ] Logger-to-tracker bridge as a breadcrumb child in `:logger:sentry`.
- [ ] `RamTracker` replaces `InMemoryTracker` and `FakeUser`; tests isolated and extended.

## Phase 5 — `:logger:sentry` (~2-3 days)

- [ ] Spike: linking Sentry Cocoa through SPM in an Xcode project that embeds a Kotlin framework.
- [ ] `SentryLogChild`: `ERROR` and `FATAL` as events, lower levels as breadcrumbs.
- [ ] Initialisation helper with `sendDefaultPii = false`, gated by `CRASH_REPORTING` consent.

## Phase 6 — `:tracker:firebase` (~2-3 days)

- [ ] Spike: `dev.gitlive:firebase-analytics` (preferred, matches the consumers' `firebase-kotlin-sdk`) against a native Android implementation with an iOS bridge; record the outcome in ADR-004.
- [ ] Firebase limits enforced by truncation and a logged warning, never by a swallowed exception.
- [ ] Collection disabled until `ANALYTICS` consent is granted.
- [ ] Release 1.1.0, with `published-library` applied to `:tracker:core`, `:logger:sentry` and `:tracker:firebase`.

## Phase 7 — Compose Multiplatform sample (~2 days)

- [ ] `:sample` for Android, iOS and Desktop, consuming the libraries through one umbrella iOS framework.
- [ ] Demo screen: log levels, consent toggles, events.
- [ ] Sample built in CI as an integration smoke test.
- [ ] Raise the Android target to Java 25 bytecode once the sample's Android build dexes it; D8 in build-tools 36 still rejects it.

## Phase 8 — Conventions and consumers (~3-4 days)

- [ ] PR to `cyrillrx/coding-conventions`: `logging-conventions.md`, library publishing rules, the latest-toolchain policy, nested module naming.
- [ ] Adopt the released libraries in each consumer, after aligning their toolchains on the latest stable versions.
