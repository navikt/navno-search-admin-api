plugins {
    kotlin("jvm") version "2.2.21" apply false
    kotlin("plugin.spring") version "2.2.21" apply false

    id("org.springframework.boot") version "3.5.7" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false

    id("com.github.ben-manes.versions") version "0.53.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}