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
    alias(libs.plugins.android.library)
}

android {
    namespace = "logger.crashlytics"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.logger.logger)

    implementation(libs.logger.crashlytics)
}
