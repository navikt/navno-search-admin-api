package no.nav.navnosearchadminapi.integrationtests

import com.nimbusds.jose.JOSEObjectType
import no.nav.navnosearchadminapi.common.repository.ContentRepository
import no.nav.navnosearchadminapi.integrationtests.config.OpensearchConfiguration
import no.nav.navnosearchadminapi.rest.aspect.HeaderCheckAspect.Companion.API_KEY_HEADER
import no.nav.navnosearchadminapi.utils.initialTestData
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cache.CacheManager
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(disabledWithoutDocker = true)
@Import(OpensearchConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 0)
@EnableMockOAuth2Server
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var repository: ContentRepository

    @Autowired
    lateinit var server: MockOAuth2Server

    @Autowired
    lateinit var cacheManager: CacheManager

    @LocalServerPort
    var serverPort: Int? = null

    @Value("\${api-key}") lateinit var apiKey: String

    fun host() = "http://localhost:$serverPort"

    fun indexCount() = repository.count()

    fun setupIndex() {
        repository.deleteAll()
        repository.saveAll(initialTestData)
    }

    fun headers(isAuthValid: Boolean = true, isApiKeyValid: Boolean = true): HttpHeaders {
        val headers = HttpHeaders()
        val token = if (isAuthValid) {
            token("azuread", "subject", "someaudience")
        } else {
            token("invalid", "invalid", "invalid")
        }
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer $token")

        if (isApiKeyValid) {
            headers.add(API_KEY_HEADER, apiKey)
        }

        return headers
    }

    private fun token(issuerId: String, subject: String, audience: String): String {
        return server.issueToken(
            issuerId,
            "theclientid",
            DefaultOAuth2TokenCallback(
                issuerId,
                subject,
                JOSEObjectType.JWT.type,
                listOf(audience),
                mapOf("acr" to "Level4"),
                3600
            )
        ).serialize()
    }
}