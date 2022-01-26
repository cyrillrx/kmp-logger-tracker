buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
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

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    kotlinOptions {
        jvmTarget = Version.jvmTarget
    }

    lintOptions {
        lintConfig = file("${project.rootDir}/lint.xml")
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":logger:lib"))

    implementation("androidx.annotation:annotation:1.3.0")
    implementation("com.google.firebase:firebase-crashlytics:18.2.7")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.7.7")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
}
