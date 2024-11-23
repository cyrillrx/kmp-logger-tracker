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

- For Android:

```sh
./gradlew assembleDebug
```

- For iOS:
  Open the `iosApp` project in Xcode and build.

## Usage

### Logger

```kotlin
Logger.addChild(SystemOutLog(Severity.DEBUG))
Logger.addChild(LogCat(Severity.DEBUG, true))

Logger.info("TAG", "Something happened")
```

### Tracker

```kotlin
Tracker.setupExceptionCatcher { t -> Logger.error("Tracker", "Caught exception", t) }
Tracker.addChild(createTracker())

val event = TrackEvent("Event Name")
Tracker.track(event)
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
