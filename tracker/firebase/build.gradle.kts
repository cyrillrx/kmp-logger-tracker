plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.cyrillrx.tracker.firebase"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.tracker.lib)

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("androidx.annotation:annotation:1.3.0")
    implementation("com.google.firebase:firebase-core:20.0.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.7.7")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
}
