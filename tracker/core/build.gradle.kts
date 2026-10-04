plugins {
    id("kmp-library")
}

base.archivesName.set("tracker")
project.version = Version.TRACKER_VERSION

kotlin {
    android {
        namespace = "com.cyrillrx.tracker"
    }
}
