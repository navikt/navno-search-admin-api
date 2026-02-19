plugins {
    val kotlinVersion = "2.2.21"
    val springBootVersion = "4.0.3"
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