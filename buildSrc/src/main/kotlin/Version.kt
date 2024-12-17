import org.gradle.api.JavaVersion

object Version {
    const val MIN_SDK = 23
    const val COMPILE_SDK = 35

    val java = JavaVersion.VERSION_17
    const val JVM_TARGET = "17"

    const val LOGGER_VERSION = "1.6.1"
    const val TRACKER_VERSION = "0.9.0"
}
