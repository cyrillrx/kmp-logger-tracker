# Changelog

All notable changes to the libraries are recorded here. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and versions follow [Semantic Versioning](https://semver.org/).

From 1.0.0 onwards, every module shares one version and is published to Maven Central under `io.github.cyrillrx` ([ADR-001](docs/adr/adr-001-distribution-via-maven-central.md)).

## [Unreleased]

### Added

- `LogEntry`, the immutable record handed to every child: severity, tag, message, throwable, attributes and timestamp.
- `RamLogChild`, an in-memory child for tests.

### Changed

- `Severity` is ordered from the least to the most severe, `VERBOSE` to `FATAL`; its `level`, `label` and `emoji` properties are gone.
- `LogChild` is an interface: `isLoggable(severity, tag)` filters, `log(entry)` writes.
- `SeverityLogChild` takes the minimum severity it accepts.
- Targets are Android, `iosArm64`, `iosSimulatorArm64` and `jvm`. The `jvm` target emits Java 25 bytecode, the Android target Java 21.
- The `desktop` target is renamed `jvm`.
- `Clock` and `Instant` come from `kotlin.time`; the kotlinx-datetime dependency is gone.

### Removed

- The `logger-crashlytics`, `tracker-amplitude`, `tracker-segment`, `device` and `notifier` modules.
- The `iosX64` target.
- `LogEvent`, which nothing used and whose name broke Firebase's 40-character limit; the tracker no longer depends on the logger.
- The per-library iOS frameworks `KMPLogger` and `KMPTracker`; consumers expose their own umbrella framework.

## Before 1.0.0

The libraries were never published under the current coordinates. Their earlier history, kept per module in `release_notes.txt` files until this changelog replaced them, is summarised below; the full notes remain in the git history.

### Logger

- **2.0.0** (2024-12-07) — Rewritten as a Kotlin Multiplatform library.
- **1.6.x** (2019) — `LogHelper` builds clickable log traces; `LogChild` becomes an abstract class; `LogWrapper` is renamed `SeverityLogChild`.
- **1.5.0** (2018) — `L` alias for `Logger`.
- **1.0.0 to 1.2.0** (2015-2016) — Severity levels, `LogCat` and `SystemOutLog` children, Java 7 compatibility.

### Tracker

- **1.0.0** (2024-12-07) — Rewritten as a Kotlin Multiplatform library.
- **0.9.0** (2019) — `TrackerChild` becomes an abstract class and absorbs `TrackWrapper`; `TrackFilter` is removed.
- **0.6.x to 0.8.x** (2016-2018) — Exception catcher protecting the host application, context injected into events, custom attributes.
- **0.1.0 to 0.5.0** (2015-2016) — Tracker component, event builders, queues and consumers.

### Firebase tracker

- **0.4.1** (2016-11-17) — Last release of the Android-only extension, plugged into Firebase Analytics; rewritten in phase 6 of the [roadmap](docs/roadmap.md).
