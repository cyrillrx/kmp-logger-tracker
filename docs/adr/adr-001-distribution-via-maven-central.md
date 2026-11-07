# ADR-001: Distribution through Maven Central

> **Status**: Accepted | **Date**: 2026-10-03 | **Context**: Realignment of the repository, before the first publication

## Decision

The libraries are published to **Maven Central** under the `groupId` **`io.github.cyrillrx`**. Kotlin packages stay `com.cyrillrx.logger` and `com.cyrillrx.tracker`.

| Module              | Coordinates                           |
|---------------------|---------------------------------------|
| `:logger:core`      | `io.github.cyrillrx:logger`           |
| `:logger:sentry`    | `io.github.cyrillrx:logger-sentry`    |
| `:tracker:core`     | `io.github.cyrillrx:tracker`          |
| `:tracker:firebase` | `io.github.cyrillrx:tracker-firebase` |

- **One version for every module.** A `vX.Y.Z` tag on `main` publishes all of them. Versioning follows semver and restarts at `1.0.0`, since the coordinates are new. A module joins the train at the version it is first published with: the logger at `1.0.0`, the tracker and the integrations at `1.1.0`. Until then a tag does not publish it.
- **Release candidates are the betas.** `X.Y.Z-rcN` versions are published to Central like any release, and consumers adopt them to validate the version before `X.Y.Z` ships. A candidate is required before `1.0.0`; after it, a release goes through one only when it carries a stability risk.
- **One source for the version.** A single version committed in the build holds it for every module, and the release fails when the tag does not match it.
- **Tooling.** The `com.vanniktech.maven.publish` plugin, applied through the `published-library` convention plugin in `buildSrc`, publishes to the Central Portal and signs in memory. `.github/workflows/release.yml` runs `publishAndReleaseToMavenCentral` on tags and creates the GitHub Release from `CHANGELOG.md` (Keep a Changelog).
- **Latest toolchain.** The libraries are always built with the latest stable Kotlin, AGP, Gradle and JDK LTS. Consumers align on it; the libraries never level down to the oldest consumer. Dependabot keeps the build current.

Consumers add nothing to their repositories block:

```kotlin
dependencies {
    implementation("io.github.cyrillrx:logger:1.0.0")
}
```

## Context

The libraries must be shared by several personal projects, present and future, each in its own repository. None of them depends on the libraries today, and nothing was ever published.

Constraints found in the consumers:

1. They resolve dependencies from `google()` and `mavenCentral()` only, sometimes with `google()` filtered by group.
2. They embed a single umbrella iOS framework through `embedAndSignAppleFrameworkForXcode`, so the libraries are consumed as Kotlin dependencies (klibs), not as XCFrameworks.
3. Sharing code across Gradle builds through a composite build has already been rejected in a consumer.
4. Their toolchains drift apart over time, depending on which project was touched last.

The questions to settle were: where to publish, under which namespace, how to version, how a consumer validates a version before it ships, where the version is written, and which toolchain to target.

## Rationale

### Why Maven Central

It is the only registry both consumers already resolve from, so adopting the libraries changes nothing in their `settings.gradle.kts`. It needs no credentials to read, locally or in CI, and it is where any Kotlin developer expects to find a library — which matters for a showcase project.

### Why `io.github.cyrillrx`

A published `groupId` is permanent. `com.cyrillrx` would tie it to a domain whose renewal is not guaranteed; `io.github.cyrillrx` is verified once through the GitHub account and never expires. A `groupId` that differs from the Kotlin package is common and has no effect on consumers.

### Why one version

The modules are released together and depend on each other: `:logger:sentry` depends on `:logger:core`, `:tracker:core` on `:logger:core`. One version removes the compatibility matrix and keeps the release workflow to a single tag.

### Why the latest toolchain

Kotlin/Native klibs compiled with a newer compiler are not guaranteed to be readable by an older one. Tracking the oldest consumer would freeze the libraries at whichever project was touched last. Every consumer belongs to the same owner, so aligning them on the latest stable release is cheaper than maintaining backward compatibility, and keeps all projects current.

## Consequences

- **Manual prerequisites** before the first release: a Central Portal account, the `io.github.cyrillrx` namespace verified, a GPG key published on a keyserver, and the repository secrets `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_IN_MEMORY_KEY` and `SIGNING_IN_MEMORY_KEY_PASSWORD`.
- **Nested modules need an explicit `artifactId`**, otherwise `:logger:core` and `:tracker:core` would both publish as `core`. The `published-library` plugin sets it ([ADR-004](adr-004-supported-backends-and-module-layout.md)).
- **A release is irreversible.** A version published to Central cannot be deleted. `publishToMavenLocal` is inspected before each release. `1.0.0` waits for a consumer to adopt its candidate (`v1.0.0-rcN`); a later release, a hotfix included, does not have to. Every candidate stays on Central. `X.Y.Z` is rebuilt from its last candidate's code with only the version changed, so a candidate validates the code rather than the published artifact.
- **A consumer behind the latest toolchain upgrades first**, before it adopts the libraries or a new release of them.
- **A Kotlin release can force a library release** when a consumer moves first. Dependabot pull requests on the build are merged promptly for that reason.
- **Public API changes are versioned**: the ABI dump checked in CI makes a breaking change visible, and a breaking change means a major version.

## Alternatives considered

**GitHub Packages** — Rejected: reading a package requires a personal access token even when the package is public, in every consumer's local setup and CI, and each consumer needs an extra repository.

**JitPack** — Rejected: building Kotlin/Native artifacts on JitPack is unreliable, builds happen on first request, and consumers need an extra repository.

**Composite build or git submodule** — Rejected: couples every consumer to a local checkout of this repository, and a consumer already rejected this model.

**`com.cyrillrx` as `groupId`** — Rejected: ties a permanent identifier to a domain that may not be renewed.

**Release candidates as private rehearsals.** Rejected: no consumer runs the version before `X.Y.Z`, so its first real use is already permanent on Central.

**A candidate before every release.** Rejected: a hotfix would wait for a consumer to adopt it.

**Snapshots.** Rejected: they live in a separate repository the consumers do not resolve from, and a snapshot changes under its consumer.

**A version derived from the tag.** Rejected: a build with no tag, such as the `publishToMavenLocal` inspection, would not carry the version that ships.

**Independent versions per module** — Rejected: a compatibility matrix and a release workflow per module, for modules that are released together anyway.

**Building against the oldest consumer's Kotlin** — Rejected: freezes the libraries on the least maintained consumer; all consumers align on the latest toolchain instead.
