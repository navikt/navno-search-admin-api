import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.0.20"
    val springBootVersion = "3.3.3"
    val springDepMgmtVersion = "1.1.6"
    val versionsVersion = "0.51.0"

    kotlin("jvm")
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDepMgmtVersion
    id("com.github.ben-manes.versions") version versionsVersion // ./gradlew dependencyUpdates to check for new versions
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    val navSecurityVersion = "5.0.5"
    val logstashVersion = "8.0"
    val opensearchVersion = "1.5.2"
    val jsoupVersion = "1.18.1"
    val opensearchTestcontainersVersion = "2.1.0"
    val testcontainersVersion = "1.20.1"
    val wiremockVersion = "4.1.4"

    implementation(project(":lib"))
    implementation("no.nav.security:token-validation-spring:$navSecurityVersion")
    implementation("org.opensearch.client:spring-data-opensearch-starter:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jsoup:jsoup:$jsoupVersion")
    testImplementation("no.nav.security:token-validation-spring-test:$navSecurityVersion")
    testImplementation("org.opensearch.client:spring-data-opensearch-test-autoconfigure:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:$wiremockVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.opensearch:opensearch-testcontainers:$opensearchTestcontainersVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "skipped", "failed")
    }
}
