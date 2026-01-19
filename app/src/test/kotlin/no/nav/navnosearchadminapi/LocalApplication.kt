package no.nav.navnosearchadminapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Profile

@SpringBootApplication(
    excludeName = ["org.springframework.boot.data.elasticsearch.autoconfigure.DataElasticsearchAutoConfiguration"]
)

@Profile("local")
class LocalApplication

fun main(args: Array<String>) {
    val springApp = SpringApplication(LocalApplication::class.java)
    springApp.setAdditionalProfiles("local")
    springApp.run(*args)
}