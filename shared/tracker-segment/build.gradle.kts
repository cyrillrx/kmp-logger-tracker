plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.cyrillrx.tracker.segment"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.shared.logger)
    implementation(projects.shared.tracker)
    implementation(projects.shared.device)

    implementation(libs.tracker.segment)
}
