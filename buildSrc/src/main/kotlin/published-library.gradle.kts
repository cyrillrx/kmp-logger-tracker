import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar

plugins {
    base
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

private val repositoryUrl = "https://github.com/cyrillrx/kmp-logger-tracker"
private val artifactId = path.removePrefix(":").replace(':', '-').removeSuffix("-core")

base.archivesName.set(artifactId)

mavenPublishing {
    configure(KotlinMultiplatform(JavadocJar.Dokka("dokkaGeneratePublicationHtml"), SourcesJar.Sources()))
    coordinates(artifactId = artifactId)
    publishToMavenCentral()
    // Local publications stay unsigned; the release workflow provides the key, and Central rejects unsigned artifacts.
    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

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
