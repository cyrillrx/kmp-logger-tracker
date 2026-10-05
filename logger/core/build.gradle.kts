plugins {
    id("kmp-library")
}

base.archivesName.set("logger")
project.version = Version.LOGGER_VERSION

kotlin {
    android {
        namespace = "com.cyrillrx.logger"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.atomicfu)
        }
    }
}
