package no.nav.navnosearchadminapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ElasticsearchDataAutoConfiguration::class])
class NavnoSearchAdminApiApplication

fun main(args: Array<String>) {
    runApplication<NavnoSearchAdminApiApplication>(*args)
}
