import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ktlint)
}

base.archivesName.set("logger")
project.version = Version.LOGGER_VERSION

kotlin {
    jvmToolchain(Version.JDK)

    android {
        namespace = "com.cyrillrx.logger"
        compileSdk = Version.COMPILE_SDK
        minSdk = Version.MIN_SDK
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.ANDROID_JVM_TARGET))
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "KMPLogger"
            isStatic = false
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.JVM_TARGET))
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
