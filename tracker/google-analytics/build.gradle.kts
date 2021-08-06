buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.8")
    }
}

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

    kotlinOptions {
        jvmTarget = Version.jvmTarget
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }

    lintOptions {
        lintConfig = file("${project.rootDir}/lint.xml")
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":tracker:lib"))

    implementation("androidx.annotation:annotation:1.2.0")
    implementation("com.google.android.gms:play-services-analytics:17.0.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:2.28.2")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
}
