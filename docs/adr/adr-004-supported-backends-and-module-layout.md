# ADR-004: Supported backends and module layout

> **Status**: Accepted | **Date**: 2026-10-03 | **Context**: Realignment of the repository, before the build modernisation

## Decision

**Backends.** Version 1 integrates two third-party services:

| Need               | Service            | Module              | Targets           |
|--------------------|--------------------|---------------------|-------------------|
| Crashes and errors | Sentry (KMP SDK)   | `:logger:sentry`    | Android, iOS, JVM |
| Product analytics  | Firebase Analytics | `:tracker:firebase` | Android, iOS      |

PostHog is the candidate if analytics is ever needed on Desktop; it is out of scope until then.

**Removed modules.** `logger-crashlytics`, `tracker-amplitude`, `tracker-segment`, `device` and `notifier` are deleted. Their history stays in git. The `androidApp` and `iosApp` demos are deleted too, replaced by the sample below.

**Layout.** Modules are nested instead of carrying compound names:

| Module              | Directory           | Published artifact                    |
|---------------------|---------------------|---------------------------------------|
| `:logger:core`      | `logger/core/`      | `io.github.cyrillrx:logger`           |
| `:logger:sentry`    | `logger/sentry/`    | `io.github.cyrillrx:logger-sentry`    |
| `:tracker:core`     | `tracker/core/`     | `io.github.cyrillrx:tracker`          |
| `:tracker:firebase` | `tracker/firebase/` | `io.github.cyrillrx:tracker-firebase` |
| `:sample`           | `sample/`           | Not published                         |

- The `published-library` convention plugin sets each `artifactId` explicitly; Gradle's default would be the leaf name (`core`, `firebase`).
- `logger/` and `tracker/` are plain directories, not projects carrying code.
- Every published module targets Android, `iosArm64`, `iosSimulatorArm64` and `jvm`, except where the service has no JVM SDK.

**Sample.** `:sample` is one Compose Multiplatform application for Android, iOS and Desktop. It consumes the libraries the way the consumers do — one umbrella iOS framework embedded through `embedAndSignAppleFrameworkForXcode` — and is built in CI as an integration test.

## Context

The repository carried eight modules besides the two libraries ([audit](../audit-2026-10.md)). All of them were Android-only and none compiled its Kotlin code: they applied no Kotlin plugin, and `SegmentTracker` and `HardwareUtils` had compile errors on top. Their SDKs were legacy: `firebase-core` without BoM, Amplitude `android-sdk` 3.x, Segment `analytics-android` 4.x.

The consumers' needs:

1. `kmp-ttrpg-companion` and `family-planner` ship on Android, iOS and Desktop; `family-planner` also runs a Ktor server.
2. `family-planner`'s ADR-003 already chose Firebase, through `firebase-kotlin-sdk`.
3. Both are personal showcase projects: free tiers matter, and Kotlin Multiplatform coverage is part of the showcase.

The conventions say module names are lower case and discourage multi-word names.

## Rationale

### Why Sentry for crashes

Its official Kotlin Multiplatform SDK covers Android, iOS and the JVM, so the same integration reports crashes from the mobile apps, the Desktop builds and the Ktor server. Crashlytics covers mobile only. The free tier fits personal projects.

### Why Firebase Analytics

`family-planner` already depends on Firebase, analytics is free without volume limits, and `dev.gitlive:firebase-analytics` exposes it to common code, consistent with the `firebase-kotlin-sdk` that ADR-003 of `family-planner` selected. Whether `:tracker:firebase` uses it or a native implementation per platform is settled by a spike at the start of its phase, and recorded here.

### Why remove rather than repair

Repairing the five modules means migrating three SDKs nobody uses, for services no consumer has chosen. Git keeps the code if one of them is ever needed again.

### Why nested modules

`:tracker:firebase` reads as "the Firebase part of the tracker" without a compound name, matches the conventions' preference for single-word names, and groups the modules of a library under one directory.

### Why one Compose Multiplatform sample

The two native demos duplicated each other, and the iOS one embedded two Kotlin frameworks — a setup no consumer uses. A Compose Multiplatform sample matches the consumers' stack and exercises the real integration path.

## Consequences

- Sentry on iOS requires the Sentry Cocoa framework to be linked in the consumer's Xcode project, through SPM since neither consumer uses CocoaPods. A spike validates the setup and each integration's README documents it.
- Firebase on iOS requires the Firebase iOS SDK to be linked the same way, and a `GoogleService-Info.plist` in the app.
- `:tracker:firebase` has no JVM target; a Desktop build that wants analytics has no backend until PostHog is integrated.
- Accessors become `projects.logger.core` and `projects.tracker.firebase`.
- The release notes of the removed modules are not carried into `CHANGELOG.md`.

## Alternatives considered

**Crashlytics** — Rejected: no Desktop or server coverage, and Sentry already covers mobile.

**Keep and migrate Amplitude and Segment** — Rejected: no consumer uses them; migrating to their Kotlin SDKs is work with no user.

**PostHog in version 1** — Rejected: no SDK for common code, and no consumer needs Desktop analytics yet.

**Flat modules with compound names (`:tracker-firebase`)** — Rejected: compound names are discouraged by the conventions.

**Code-carrying parent projects (`:logger` with `:logger:sentry` inside)** — Rejected: mixes a module's sources with its sub-projects in one directory.

**Repair the two native demos** — Rejected: they need an umbrella framework anyway, and a Compose Multiplatform sample covers Android, iOS and Desktop with one codebase.
