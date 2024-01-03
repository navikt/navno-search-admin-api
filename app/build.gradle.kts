import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val versions = object {
        val kotlin = "1.9.22"
        val springBoot = "3.2.1"
        val springDepMgmt = "1.1.4"
        val versions = "0.50.0"
    }

    kotlin("jvm")
    kotlin("plugin.spring") version (versions.kotlin)

    id("org.springframework.boot") version (versions.springBoot)
    id("io.spring.dependency-management") version (versions.springDepMgmt)
    id("com.github.ben-manes.versions") version (versions.versions) // ./gradlew dependencyUpdates to check for new versions
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    val versions = object {
        val coroutines = "1.7.3"
        val navSecurity = "3.2.0"
        val logstash = "7.4"
        val opensearch = "1.3.0"
        val jsoup = "1.10.2"
        val opensearchTestcontainers = "2.0.1"
        val testcontainers = "1.19.3"
    }

    implementation(project(":lib"))
    implementation("no.nav.security:token-validation-spring:${versions.navSecurity}")
    implementation("org.opensearch.client:spring-data-opensearch-starter:${versions.opensearch}") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("net.logstash.logback:logstash-logback-encoder:${versions.logstash}")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jsoup:jsoup:${versions.jsoup}")
    testImplementation("no.nav.security:token-validation-spring-test:${versions.navSecurity}")
    testImplementation("org.opensearch.client:spring-data-opensearch-test-autoconfigure:${versions.opensearch}") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.0.4")
    testImplementation("org.testcontainers:junit-jupiter:${versions.testcontainers}")
    testImplementation("org.opensearch:opensearch-testcontainers:${versions.opensearchTestcontainers}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "skipped", "failed")
    }
}
