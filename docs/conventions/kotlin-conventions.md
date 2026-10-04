# Kotlin Conventions

> [!IMPORTANT]
> **Canonical source of truth** (shared, project-agnostic): [`conventions/kotlin-conventions.md`](https://github.com/cyrillrx/coding-conventions/blob/main/conventions/kotlin-conventions.md) — do not duplicate here.

The canonical document applies as-is. The UI layer's [Compose conventions](https://github.com/cyrillrx/coding-conventions/blob/main/conventions/compose-conventions.md) (MVVM, navigation, lifecycle) only concern the `:sample` module, so the project does not enable their plugin.

## Project-specific additions

- **Targets** — Android, `iosArm64`, `iosSimulatorArm64` and `jvm`. These cover every consumer: Android, iOS, Desktop and JVM servers.
- **Toolchain** — always the latest stable Kotlin, AGP, Gradle and JDK LTS. Consumers align on it ([ADR-001](../adr/adr-001-distribution-via-maven-central.md)).
- **Public API** — `explicitApi()` is on, every public declaration carries KDoc, and the ABI dump is checked in CI.
- **Module naming** — nested modules instead of compound names: `:tracker:firebase`, not `:tracker-firebase`. The published `artifactId` is set explicitly ([ADR-004](../adr/adr-004-supported-backends-and-module-layout.md)).
- **Package** — `com.cyrillrx.logger` and `com.cyrillrx.tracker`. The Maven `groupId` is `io.github.cyrillrx`.
- **Test location** — tests live in each module's `src/commonTest/` and run on the JVM and iOS simulator targets. In-memory children are named `Ram*` and live in the main source set.
- **Formatting** — ktlint, configured by the repository-root [`.editorconfig`](../../.editorconfig), itself copied from the shared conventions repository.
