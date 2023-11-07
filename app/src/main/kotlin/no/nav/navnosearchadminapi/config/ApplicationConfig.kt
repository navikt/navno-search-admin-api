package no.nav.navnosearchadminapi.config

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
@EnableCaching
@EnableJwtTokenValidation
class ApplicationConfig {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? {
        return builder.build()
    }
}