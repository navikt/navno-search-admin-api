package no.nav.navnosearchadminapi

import org.opensearch.spring.boot.autoconfigure.OpenSearchRestHighLevelClientAutoConfiguration
import org.opensearch.spring.boot.autoconfigure.ReactiveOpenSearchClientAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        OpenSearchRestHighLevelClientAutoConfiguration::class,
        ReactiveOpenSearchClientAutoConfiguration::class,
    ]
)
class NavnoSearchAdminApiApplication

fun main(args: Array<String>) {
    runApplication<NavnoSearchAdminApiApplication>(*args)
}
