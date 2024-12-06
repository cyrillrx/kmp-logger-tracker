plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.cyrillrx.tracker.amplitude"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.logger.logger)
    implementation(projects.tracker.tracker)
    implementation(projects.device)

    implementation(libs.tracker.amplitude)
}
