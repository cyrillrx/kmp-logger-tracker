enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmp-logger-tracker"

include(
    ":androidApp",
    ":shared:notifier",
    ":shared:device",
    ":shared:logger",
    ":shared:logger-crashlytics",
    ":shared:tracker",
    ":shared:tracker-firebase",
    ":shared:tracker-amplitude",
    ":shared:tracker-segment",
)
