import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("kmp-library")
}

base.archivesName.set("tracker")

kotlin {
    // TODO(phase 4): drop this opt-out once the tracker API is rewritten.
    explicitApi = ExplicitApiMode.Disabled

    android {
        namespace = "com.cyrillrx.tracker"
    }
}
