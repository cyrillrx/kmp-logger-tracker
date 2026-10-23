import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    id("kmp-library")
}

base.archivesName.set("logger")
project.version = Version.LOGGER_VERSION

kotlin {
    applyDefaultHierarchyTemplate {
        common {
            group("jvmAndAndroid") {
                withJvm()
                withCompilations { it.platformType == KotlinPlatformType.androidJvm }
            }
        }
    }

    android {
        namespace = "com.cyrillrx.logger"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.atomicfu)
        }
    }
}
