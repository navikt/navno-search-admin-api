package no.nav.navnosearchadminapi

import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration


@SpringBootApplication(exclude = [ElasticsearchDataAutoConfiguration::class])
@EnableMockOAuth2Server
class LocalApplication

fun main(args: Array<String>) {
    val springApp = SpringApplication(LocalApplication::class.java)
    springApp.setAdditionalProfiles("local")
    springApp.run(*args)
}
