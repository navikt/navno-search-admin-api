package no.nav.navnosearchadminapi.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import java.time.Clock

@Configuration
@EnableCaching
class ApplicationConfig {
    @Bean
    fun restClient(builder: RestClient.Builder): RestClient {
        return builder.build()
    }

    @Bean
    fun clock(): Clock {
        return Clock.systemDefaultZone()
    }
}