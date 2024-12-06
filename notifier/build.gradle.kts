plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.cyrillrx.notifier"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(libs.androidx.annotation)
}
