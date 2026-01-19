plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.github.ben-manes.versions")
}

dependencies {
    val logstashVersion = "9.0"
    val opensearchVersion = "3.0.0"
    val jsoupVersion = "1.22.1"
    val opensearchTestcontainersVersion = "4.1.0"
    val testcontainersVersion = "1.21.4"
    val wiremockSpringBootVersion = "4.0.8"
    val kotestVersion = "6.0.7"
    val jacksonVersion = "2.20.1"

    implementation(project(":lib"))

    implementation("org.opensearch.client:spring-data-opensearch-starter:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aspectj")
    implementation("org.springframework.boot:spring-boot-starter-restclient")

    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.jsoup:jsoup:$jsoupVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testImplementation("org.opensearch.client:spring-data-opensearch-test-autoconfigure:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-resttestclient")

    testImplementation("org.wiremock.integrations:wiremock-spring-boot:$wiremockSpringBootVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.opensearch:opensearch-testcontainers:$opensearchTestcontainersVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
}