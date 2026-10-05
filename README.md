# Logger and Tracker project

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

1. Clone the repository:

```sh
git clone https://github.com/cyrillrx/kmp-logger-tracker.git
cd kmp-logger-tracker
```

2. Open the project in Android Studio.
3. Sync the project with Gradle files.

### Building the Project

```sh
./gradlew build
```

## Usage

### Logger

```kotlin
Log.install(
    CompositeLogger(
        children = listOf(LogCat(Severity.DEBUG, clickableLogs = true)),
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
