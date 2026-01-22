import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21" apply false
    kotlin("plugin.spring") version "2.2.21" apply false

    id("org.springframework.boot") version "4.0.1" apply false
    id("com.github.ben-manes.versions") version "0.53.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withId("java") {
        extensions.configure<JavaPluginExtension>("java") {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }
}