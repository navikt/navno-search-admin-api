plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("com.github.ben-manes.versions")
}

dependencies {
    val springBootVersion = "4.0.1"
    val logstashVersion = "9.0"
    val opensearchVersion = "2.0.3"
    val jsoupVersion = "1.22.1"
    val opensearchTestcontainersVersion = "4.1.0"
    val testcontainersVersion = "1.21.4"
    val wiremockVersion = "4.3.0"
    val kotestVersion = "6.0.7"
    val jacksonVersion = "2.21.0"

    implementation(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    implementation(project(":lib"))

    implementation("org.opensearch.client:spring-data-opensearch-starter:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }

    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-cache:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-aspectj:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-restclient:${springBootVersion}")

    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.jsoup:jsoup:$jsoupVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testImplementation("org.opensearch.client:spring-data-opensearch-test-autoconfigure:$opensearchVersion") {
        exclude("org.opensearch.client", "opensearch-rest-client-sniffer")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:$wiremockVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.opensearch:opensearch-testcontainers:$opensearchTestcontainersVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
}