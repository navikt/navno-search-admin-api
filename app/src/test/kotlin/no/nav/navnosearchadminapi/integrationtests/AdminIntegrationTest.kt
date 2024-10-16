package no.nav.navnosearchadminapi.integrationtests

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import no.nav.navnosearchadminapi.dto.outbound.SaveContentResponse
import no.nav.navnosearchadminapi.exception.handler.ErrorResponse
import no.nav.navnosearchadminapi.utils.TEAM_NAME
import no.nav.navnosearchadminapi.utils.dummyContentDto
import no.nav.navnosearchadminapi.utils.mockedKodeverkResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity

class AdminIntegrationTest : AbstractIntegrationTest() {

    @BeforeEach
    fun setup() {
        WireMock.reset()
        setupIndex()
        mockAzuread()
        stubFor(
            get(urlPathMatching("/kodeverk")).willReturn(
                aResponse().withStatus(HttpStatus.OK.value())
                    .withBody(objectMapper.writeValueAsString(mockedKodeverkResponse))
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            )
        )
    }

    @Test
    fun testFetchingContent() {
        val response: ResponseEntity<Map<String, Any>> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME?page=0",
            HttpMethod.GET,
            HttpEntity<Any>(headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body?.get("totalElements") shouldBe 10
    }

    @Test
    fun testFetchingContentWithMissingRequestParam() {
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.GET,
            HttpEntity<Any>(headers()),
        )

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body?.message shouldBe "Påkrevd request parameter mangler: page"
    }

    @Test
    fun testSavingContent() {
        val content = dummyContentDto()

        val response: ResponseEntity<SaveContentResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        indexCount() shouldBe 11L
        repository.existsById("$TEAM_NAME-${content.id}").shouldBeTrue()
    }

    @Test
    fun testSavingContentWithInvalidApiKey() {
        val content = dummyContentDto()

        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers(isApiKeyValid = false)),
        )

        response.statusCode shouldBe HttpStatus.UNAUTHORIZED
    }

    @Test
    fun testSavingContentWithMissingId() {
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(dummyContentDto(id = null)), headers()),
        )

        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body?.message shouldBe "id er påkrevd for alle dokumenter"
    }

    @Test
    fun testSavingContentWithMissingRequiredField() {
        val content = dummyContentDto(ingress = null)

        val response: ResponseEntity<SaveContentResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body?.numberOfIndexedDocuments shouldBe 0
        response.body?.numberOfFailedDocuments shouldBe 1
        response.body!!.validationErrors[content.id]!!.first() shouldBe "Påkrevd felt mangler: ingress"
    }

    @Test
    fun testSavingContentWithInvalidLanguage() {
        val response: ResponseEntity<SaveContentResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(dummyContentDto(language = "røverspråk")), headers()),
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body!!.validationErrors["11"]!!.first() shouldBe "Ugyldig verdi for metadata.language: røverspråk. Må være tobokstavs språkkode fra kodeverk-api."
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

        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), headers()),
        )

        response.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
        response.body?.message shouldBe "Ukjent feil"
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
    }
}