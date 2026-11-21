# Logger and Tracker project

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cyrillrx/logger)](https://central.sonatype.com/artifact/io.github.cyrillrx/logger)

## Overview

The project consists of two Kotlin Multiplatform (KMP) libraries: Logger and Tracker.
The Logger library is designed for logging events with different severity levels.
The Tracker library is used for tracking events across different platforms.
Both libraries utilize a common codebase with platform-specific implementations to handle
device-specific functionalities.

## Project Structure

- `commonMain`: Contains common code shared between platforms.
- `androidMain`: Contains Android-specific implementations.
- `iosMain`: Contains iOS-specific implementations.

## Getting Started

### Installation

The Logger is published on Maven Central. Add it to the common source set of a Kotlin Multiplatform module:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.cyrillrx:logger:<version>")
        }
    }
}
```

It targets Android, the JVM, `iosArm64` and `iosSimulatorArm64`, and is built with the latest stable toolchain: a consumer aligns on it before adopting a release ([ADR-001](docs/adr/adr-001-distribution-via-maven-central.md)).

The Tracker is not published yet.

### Building the Project

```sh
git clone https://github.com/cyrillrx/kmp-logger-tracker.git
cd kmp-logger-tracker
./gradlew build
```

## Usage

### Logger

```kotlin
Log.install(
    CompositeLogger(
        children = listOf(ConsoleChild(Severity.DEBUG, clickableLogs = true)),
        onChildError = { child, error -> println("$child failed: $error") },
    ),
)

Log.info("Sync") { "Something happened" }
Log.error("Sync", throwable = error, attributes = mapOf("item" to id)) { "Upload failed" }
```

A class can also receive a `Logger` and stay independent from the global `Log` facade; tests then pass a `CompositeLogger` holding a `RamLogChild`.

### Tracker

```kotlin
Tracker.setupExceptionCatcher { t -> Log.error("Tracker", throwable = t) { "Caught exception" } }
Tracker.addChild(createTracker())

val event = TrackEvent("Event Name")
Tracker.track(event)
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
