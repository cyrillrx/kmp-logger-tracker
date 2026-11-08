plugins {
    id("kmp-library")
    base
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
    signing
}

private val repositoryUrl = "https://github.com/cyrillrx/kmp-logger-tracker"

mavenPublishing {
    coordinates(artifactId = base.archivesName.get())
    publishToMavenCentral()
    signAllPublications()

    pom {
        url.set(repositoryUrl)
        licenses {
            license {
                name.set("The MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("cyrillrx")
                name.set("Cyril Leroux")
                url.set("https://github.com/cyrillrx")
            }
        }
        scm {
            url.set(repositoryUrl)
            connection.set("scm:git:$repositoryUrl.git")
            developerConnection.set("scm:git:ssh://git@github.com/cyrillrx/kmp-logger-tracker.git")
        }
    }
}

signing {
    isRequired = gradle.startParameter.taskNames.none { it.endsWith("ToMavenLocal") }
}
