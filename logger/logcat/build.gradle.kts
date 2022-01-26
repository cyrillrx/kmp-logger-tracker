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

    lintOptions {
        lintConfig = file("../lint.xml")
    }

    kotlinOptions {
        jvmTarget = Version.jvmTarget
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":logger:lib"))

    implementation("androidx.annotation:annotation:1.3.0")
}
