// TODO(#52): apply ktlint to buildSrc.
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
    implementation(libs.kover.gradle.plugin)
    implementation(libs.maven.publish.gradle.plugin)
    implementation(libs.dokka.gradle.plugin)
}
