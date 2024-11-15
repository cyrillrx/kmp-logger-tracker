plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.cyrillrx.tracker.amplitude"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }
}

dependencies {
    implementation(projects.logger.lib)
    implementation(projects.tracker.lib)
    implementation(projects.device)

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.amplitude:android-sdk:2.35.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.7.7")

    androidTestImplementation ("androidx.annotation:annotation:1.3.0")
    androidTestImplementation ("androidx.test:runner:1.4.0")
    androidTestImplementation ("androidx.test:rules:1.4.0")
}
