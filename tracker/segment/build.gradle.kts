plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.cyrillrx.tracker.segment"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.logger.logger)
    implementation(projects.tracker.tracker)
    implementation(projects.device)

    implementation(libs.tracker.segment)
}
