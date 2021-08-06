val moduleVersion = "0.9.0"
project.version = moduleVersion

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Version.compileSdk)
    buildToolsVersion(Version.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
        versionName(moduleVersion)
    }

    kotlinOptions {
        // Needed for all xxx.lib modules to prevent duplicate file "META-INF/*lib.kotlin_module"
        moduleName = "com.cyrillrx.traker"
        jvmTarget = Version.jvmTarget
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":logger:lib"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}