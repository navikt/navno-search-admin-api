import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.2.21"
    val springBootVersion = "4.0.1"
    val springDepMgmtVersion = "1.1.7"
    val versionsVersion = "0.53.0"

    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false

    id("org.springframework.boot") version springBootVersion apply false
    id("io.spring.dependency-management") version springDepMgmtVersion apply false
    id("com.github.ben-manes.versions") version versionsVersion apply false

    `java-library`
    `maven-publish`
}

extra["opensearchVersion"] = "3.0.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withId("java") {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            testLogging {
                exceptionFormat = TestExceptionFormat.FULL
                events("passed", "skipped", "failed")
            }
        }
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }
}