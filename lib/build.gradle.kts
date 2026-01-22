plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("com.github.ben-manes.versions")
}

dependencies {
    val springBootVersion = "4.0.1"
    val opensearchVersion = "2.0.3"

    api(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

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