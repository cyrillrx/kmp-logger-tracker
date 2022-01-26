plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Version.compileSdk)

    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }

    kotlinOptions {
        jvmTarget = Version.jvmTarget
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }

    lintOptions {
        lintConfig = file("${project.rootDir}/lint.xml")
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":logger:lib"))
    implementation(project(":tracker:lib"))
    implementation(project(":device"))

    implementation("com.segment.analytics.android:analytics:4.3.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.7.7")

    androidTestImplementation("androidx.annotation:annotation:1.3.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
}
