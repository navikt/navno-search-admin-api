package no.nav.navnosearchadminapi.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Clock

@Configuration
@EnableCaching
class ApplicationConfig {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? {
        return builder.build()
    }

    @Bean
    fun clock(): Clock {
        return Clock.systemDefaultZone()
    }
}