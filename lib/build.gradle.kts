import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val versions = object {
        val kotlin = "1.9.0"
        val versions = "0.49.0"
    }

    kotlin("jvm") version (versions.kotlin)
    id("com.github.ben-manes.versions") version (versions.versions) // ./gradlew dependencyUpdates to check for new versions
    `java-library`
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    val versions = object {
        val opensearch = "1.2.0"
    }
    implementation("org.opensearch.client:spring-data-opensearch-starter:${versions.opensearch}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

val libraryVersion: String = properties["lib_version"]?.toString() ?: "latest-local"

publishing {
    repositories{
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/navikt/tms-utkast")
            credentials {
                username = "x-access-token"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("gpr") {
            groupId = "no.nav.tms.utkast"
            artifactId = "builder"
            version = libraryVersion
            from(components["java"])
        }
    }
}