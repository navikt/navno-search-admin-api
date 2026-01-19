plugins {
    kotlin("jvm")
    id("com.github.ben-manes.versions")

    `java-library`
    `maven-publish`
}

dependencies {
    val opensearchVersion = "3.0.0"

    implementation("org.opensearch.client:spring-data-opensearch-starter:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
}

val libraryVersion: String = properties["lib_version"]?.toString() ?: "latest-local"

publishing {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/navikt/navno-search-admin-api")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("gpr") {
            groupId = "no.nav.navnosearchadminapi"
            artifactId = "common"
            version = libraryVersion
            from(components["java"])
        }
    }
}