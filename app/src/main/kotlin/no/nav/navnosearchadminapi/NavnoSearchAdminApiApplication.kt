package no.nav.navnosearchadminapi

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication(exclude = [ElasticsearchDataAutoConfiguration::class])
@EnableCaching
@EnableJwtTokenValidation
class NavnoSearchAdminApiApplication

fun main(args: Array<String>) {
    runApplication<NavnoSearchAdminApiApplication>(*args)
}
