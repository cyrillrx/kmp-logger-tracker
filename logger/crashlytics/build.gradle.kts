buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("androidx.annotation:annotation:1.3.0")
    implementation("com.google.firebase:firebase-crashlytics:18.2.7")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.7.7")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
}
