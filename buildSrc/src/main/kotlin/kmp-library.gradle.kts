import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jlleitschuh.gradle.ktlint")
}

kotlin {
    jvmToolchain(Version.JDK)

    android {
        compileSdk = Version.COMPILE_SDK
        minSdk = Version.MIN_SDK
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.ANDROID_JVM_TARGET))
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.JVM_TARGET))
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

ktlint {
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
