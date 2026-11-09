import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    id("published-library")
}

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
