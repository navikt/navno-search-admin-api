package no.nav.navnosearchadminapi.integrationtests.config

import no.nav.navnosearchadminapi.utils.fixedNow
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.time.Clock

@TestConfiguration
class ClockConfig {
    @Bean
    @Primary
    fun fixedClock(): Clock {
        return fixedNow.let { Clock.fixed(it.toInstant(), it.zone) }
    }
}