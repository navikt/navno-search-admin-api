import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val versionsVersion = "0.51.0"

    kotlin("jvm")
    id("com.github.ben-manes.versions") version versionsVersion // ./gradlew dependencyUpdates to check for new versions
    `java-library`
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    val opensearchVersion = "1.3.0"

    implementation("org.opensearch.client:spring-data-opensearch-starter:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
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