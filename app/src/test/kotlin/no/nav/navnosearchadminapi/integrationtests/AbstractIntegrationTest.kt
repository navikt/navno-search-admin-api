package no.nav.navnosearchadminapi.integrationtests

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import no.nav.navnosearchadminapi.repository.ContentRepository
import no.nav.navnosearchadminapi.consumer.azuread.dto.outbound.TokenResponse
import no.nav.navnosearchadminapi.integrationtests.config.ClockConfig
import no.nav.navnosearchadminapi.integrationtests.config.OpensearchConfig
import no.nav.navnosearchadminapi.rest.aspect.HeaderCheckAspect.Companion.API_KEY_HEADER
import no.nav.navnosearchadminapi.utils.initialTestData
import no.nav.navnosearchadminapi.utils.mockedKodeverkResponse
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cache.CacheManager
import org.wiremock.spring.EnableWireMock
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(disabledWithoutDocker = true)
@Import(OpensearchConfig::class, ClockConfig::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@EnableWireMock()
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var repository: ContentRepository

    @Autowired
    lateinit var cacheManager: CacheManager

    @LocalServerPort
    var serverPort: Int? = null

    @Value("\${api-key}")
    lateinit var apiKey: String

    protected fun host() = "http://localhost:$serverPort"

    protected fun indexCount() = repository.count()

    protected fun setupIndex() {
        repository.deleteAll()
        repository.saveAll(initialTestData)
    }

    protected fun mockAzuread() {
        stubFor(
            post(urlPathMatching("/azuread")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value())
                    .withBody(objectMapper.writeValueAsString(TokenResponse(accessToken = "token")))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        )
    }

    protected fun mockKodeverk(status: HttpStatus = HttpStatus.OK) {
        when (status) {
            HttpStatus.OK -> stubFor(
                WireMock.get(urlPathMatching("/kodeverk")).willReturn(
                    aResponse().withStatus(HttpStatus.OK.value())
                        .withBody(objectMapper.writeValueAsString(mockedKodeverkResponse))
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                )
            )

            HttpStatus.INTERNAL_SERVER_ERROR -> stubFor(
                WireMock.get(urlPathMatching("/kodeverk")).willReturn(
                    aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                )
            )

            else -> error("HttpStatus $status ikke støttet")
        }
    }

    protected fun get(path: String, headers: HttpHeaders = headers()): ResponseEntity<String> {
        return restTemplate.exchange(
            "${host()}/$path",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
        )
    }

    protected fun <T> post(path: String, content: T, headers: HttpHeaders = headers()): ResponseEntity<String> {
        return restTemplate.exchange(
            "${host()}/$path",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers),
        )
    }

    protected fun delete(path: String, headers: HttpHeaders = headers()): ResponseEntity<String> {
        return restTemplate.exchange(
            "${host()}/$path",
            HttpMethod.DELETE,
            HttpEntity<Any>(headers),
        )
    }

    protected fun headers(isApiKeyValid: Boolean = true): HttpHeaders {
        return HttpHeaders().apply { if (isApiKeyValid) add(API_KEY_HEADER, apiKey) }
    }

    protected fun readFile(name: String): String {
        return {}.javaClass.getResource(name)!!.readText()
    }
}