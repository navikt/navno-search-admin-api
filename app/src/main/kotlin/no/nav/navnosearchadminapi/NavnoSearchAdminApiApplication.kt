package no.nav.navnosearchadminapi

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableJwtTokenValidation
class NavnoSearchAdminApiApplication

fun main(args: Array<String>) {
    runApplication<NavnoSearchAdminApiApplication>(*args)
}
