package no.nav.navnosearchadminapi

import com.nimbusds.jose.JOSEObjectType
import no.nav.navnosearchadminapi.common.repository.ContentRepository
import no.nav.navnosearchadminapi.utils.initialTestData
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server
import org.junit.jupiter.api.extension.ExtendWith
import org.opensearch.testcontainers.OpensearchContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@Testcontainers(disabledWithoutDocker = true, parallel = true)
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@AutoConfigureWireMock(port = 0)
@EnableMockOAuth2Server
abstract class AbstractIntegrationTest {

    val logger: Logger = LoggerFactory.getLogger(AbstractIntegrationTest::class.java)

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var repository: ContentRepository

    @Autowired
    lateinit var server: MockOAuth2Server

    @LocalServerPort
    var serverPort: Int? = null

    fun host() = "http://localhost:$serverPort"

    fun indexCount() = repository.count()

    fun setupIndex() {
        repository.deleteAll()
        repository.saveAll(initialTestData)
    }

    fun validAuthHeader(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token("azuread", "subject", "someaudience"))
        return headers
    }

    private fun token(issuerId: String, subject: String, audience: String): String {
        val token = server.issueToken(
            issuerId,
            "theclientid",
            DefaultOAuth2TokenCallback(
                issuerId,
                subject,
                JOSEObjectType.JWT.type,
                listOf(audience),
                emptyMap(),
                3600
            )
        )
        logger.info("Issuer: ${token.jwtClaimsSet.issuer}")
        return token.serialize()
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of("opensearch.uris=" + opensearch.getHttpHostAddress())
                .applyTo(configurableApplicationContext.environment)
        }
    }

    companion object {
        @Container
        val opensearch: OpensearchContainer = OpensearchContainer("opensearchproject/opensearch:2.0.0")
            .withStartupAttempts(5)
            .withStartupTimeout(Duration.ofMinutes(2))
    }
}