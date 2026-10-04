plugins {
    id("com.android.library")
}

android {
    namespace = "com.cyrillrx.tracker.firebase"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.tracker.core)
    implementation(libs.tracker.firebase)
}
