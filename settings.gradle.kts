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

rootProject.name = "logger_tracker"

include(
    ":androidApp",
    ":notifier",
    ":device",
    ":logger:logger",
    ":logger:crashlytics",
    ":tracker:tracker",
    ":tracker:firebase",
    ":tracker:amplitude",
    ":tracker:segment",
)
