buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.logger.crashlytics.gradle)
    }
}

plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "logger.crashlytics"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.logger.lib)

    implementation(libs.logger.crashlytics)
}
