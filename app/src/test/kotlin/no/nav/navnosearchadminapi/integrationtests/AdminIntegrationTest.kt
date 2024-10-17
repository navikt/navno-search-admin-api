package no.nav.navnosearchadminapi.integrationtests

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import no.nav.navnosearchadminapi.utils.TEAM_NAME
import no.nav.navnosearchadminapi.utils.dummyContentDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AdminIntegrationTest : AbstractIntegrationTest() {

    @BeforeEach
    fun setup() {
        setupIndex()
        mockAzuread()
        mockKodeverk()
    }

    @AfterEach
    fun teardown() {
        WireMock.reset()
    }

    @Test
    fun testFetchingContent() {
        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME?page=0",
            HttpMethod.GET,
            HttpEntity<Any>(headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body!! shouldEqualJson readFile("/json/fetch-content.json")
    }

    @Test
    fun testFetchingContentWithMissingRequestParam() {
        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.GET,
            HttpEntity<Any>(headers()),
        )

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body!! shouldEqualJson readFile("/json/fetch-content-missing-page-param.json")
    }

    @Test
    fun testSavingContent() {
        val content = dummyContentDto()

        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body!! shouldEqualJson readFile("/json/save-content.json")

        indexCount() shouldBe 11L
        repository.existsById("$TEAM_NAME-${content.id}").shouldBeTrue()
    }

    @Test
    fun testSavingContentWithInvalidApiKey() {
        val content = dummyContentDto()

        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers(isApiKeyValid = false)),
        )

        response.statusCode shouldBe HttpStatus.UNAUTHORIZED
        response.body!! shouldEqualJson readFile("/json/save-content-invalid-api-key.json")
    }

    @Test
    fun testSavingContentWithMissingId() {
        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(dummyContentDto(id = null)), headers()),
        )

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body!! shouldEqualJson readFile("/json/save-content-missing-id.json")
    }

    @Test
    fun testSavingContentWithMissingRequiredField() {
        val content = dummyContentDto(ingress = null)

        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body!! shouldEqualJson readFile("/json/save-content-missing-ingress.json")
    }

    @Test
    fun testSavingContentWithInvalidLanguage() {
        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(dummyContentDto(language = "røverspråk")), headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body!! shouldEqualJson readFile("/json/save-content-invalid-language.json")
    }

    @Test
    fun testSavingContentWithServerError() {
        cacheManager.getCache("spraakkoder")?.clear()

        stubFor(
            get(urlPathMatching("/kodeverk")).willReturn(
                aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            )
        )

        val content = dummyContentDto()

        val response: ResponseEntity<String> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers()),
        )

        response.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
        response.body!! shouldEqualJson readFile("/json/save-content-server-error.json")
    }

    @Test
    fun testDeletingContent() {
        val deletedId = "1"
        val response: ResponseEntity<String> =
            restTemplate.exchange(
                "${host()}/content/$TEAM_NAME/$deletedId",
                HttpMethod.DELETE,
                HttpEntity<Any>(headers()),
            )


        response.statusCode shouldBe HttpStatus.OK
        response.body!! shouldBe readFile("/json/delete-content.txt")

        indexCount() shouldBe 9L
        repository.existsById("$TEAM_NAME-$deletedId").shouldBeFalse()
    }

    @Test
    fun testDeletingContentForMissingApp() {
        val deletedId = "1"
        val teamName = "missing-team"
        val response: ResponseEntity<String> =
            restTemplate.exchange(
                "${host()}/content/$teamName/$deletedId",
                HttpMethod.DELETE,
                HttpEntity<Any>(headers()),
            )

        response.statusCode shouldBe HttpStatus.OK
        response.body!! shouldBe readFile("/json/delete-content-missing-team.txt")
    }
}