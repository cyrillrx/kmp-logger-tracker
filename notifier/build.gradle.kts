plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Version.compileSdk)

    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    implementation("androidx.annotation:annotation:1.2.0")
}
