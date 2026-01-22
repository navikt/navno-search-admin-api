package no.nav.navnosearchadminapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    excludeName = ["org.springframework.boot.data.elasticsearch.autoconfigure.DataElasticsearchAutoConfiguration"]
)
class NavnoSearchAdminApiApplication

fun main(args: Array<String>) {
    runApplication<NavnoSearchAdminApiApplication>(*args)
}