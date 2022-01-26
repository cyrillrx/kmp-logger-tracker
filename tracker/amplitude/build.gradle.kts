plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = Version.compileSdk

    defaultConfig {
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk
    }

    kotlinOptions {
        jvmTarget = Version.jvmTarget
    }

    lintOptions {
        lintConfig = file("../lint.xml")
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":logger:lib"))
    implementation(project(":tracker:lib"))
    implementation(project(":device"))

    implementation("com.amplitude:android-sdk:2.35.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.7.7")

    androidTestImplementation ("androidx.annotation:annotation:1.3.0")
    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation ("androidx.test:rules:1.4.0")
}
