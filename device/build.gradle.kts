plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.cyrillrx.device"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
    compileOptions {
        sourceCompatibility = Version.java
        targetCompatibility = Version.java
    }
}
