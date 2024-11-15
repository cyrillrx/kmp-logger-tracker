plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "logger.logcat"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.logger.lib)
}
