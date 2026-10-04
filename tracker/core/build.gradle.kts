import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ktlint)
}

base.archivesName.set("tracker")
project.version = Version.TRACKER_VERSION

kotlin {
    jvmToolchain(Version.JDK)

    android {
        namespace = "com.cyrillrx.tracker"
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
            baseName = "KMPTracker"
            isStatic = false
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.JVM_TARGET))
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.logger.core)
        }
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
