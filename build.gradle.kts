plugins {
    val kotlinVersion = "2.3.10"
    val springBootVersion = "3.5.7"
    val springDepMgmtVersion = "1.1.7"
    val versionsVersion = "0.53.0"

    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false

    id("org.springframework.boot") version springBootVersion apply false
    id("io.spring.dependency-management") version springDepMgmtVersion apply false
    id("com.github.ben-manes.versions") version versionsVersion apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}