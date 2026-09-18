package no.nav.navnosearchadminapi.integrationtests

import no.nav.navnosearchadminapi.repository.ContentRepository
import no.nav.navnosearchadminapi.integrationtests.config.ClockConfig
import no.nav.navnosearchadminapi.integrationtests.config.OpensearchConfig
import no.nav.navnosearchadminapi.rest.aspect.HeaderCheckAspect.Companion.API_KEY_HEADER
import no.nav.navnosearchadminapi.utils.initialTestData
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestClient
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(disabledWithoutDocker = true)
@Import(OpensearchConfig::class, ClockConfig::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
abstract class AbstractIntegrationTest {

    private val restClient = RestClient.create()

    @Autowired
    lateinit var repository: ContentRepository

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

    protected fun get(path: String, headers: HttpHeaders = headers()): ResponseEntity<String> {
        return restClient.get()
            .uri("${host()}/$path")
            .headers { it.addAll(headers) }
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ -> }
            .toEntity(String::class.java)
    }

    protected fun <T> post(path: String, content: T, headers: HttpHeaders = headers()): ResponseEntity<String> {
        return restClient.post()
            .uri("${host()}/$path")
            .headers { it.addAll(headers) }
            .body(listOf(content))
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ -> }
            .toEntity(String::class.java)
    }

    protected fun delete(path: String, headers: HttpHeaders = headers()): ResponseEntity<String> {
        return restClient.delete()
            .uri("${host()}/$path")
            .headers { it.addAll(headers) }
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, _ -> }
            .toEntity(String::class.java)
    }

    protected fun headers(isApiKeyValid: Boolean = true): HttpHeaders {
        return HttpHeaders().apply { if (isApiKeyValid) add(API_KEY_HEADER, apiKey) }
    }

    protected fun readFile(name: String): String {
        return {}.javaClass.getResource(name)!!.readText()
    }
}