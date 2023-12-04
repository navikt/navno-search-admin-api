package no.nav.navnosearchadminapi.integrationtests

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import no.nav.navnosearchadminapi.dto.outbound.SaveContentResponse
import no.nav.navnosearchadminapi.exception.handler.ErrorResponse
import no.nav.navnosearchadminapi.utils.TEAM_NAME
import no.nav.navnosearchadminapi.utils.dummyContentDto
import no.nav.navnosearchadminapi.utils.mockedKodeverkResponse
import org.assertj.core.api.Assertions.assertThat
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

    val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        WireMock.reset()
        setupIndex()
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
            HttpEntity<Any>(authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.get("totalElements")).isEqualTo(10)
    }

    @Test
    fun testFetchingContentWithMissingRequestParam() {
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.GET,
            HttpEntity<Any>(authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.message).isEqualTo("Påkrevd request parameter mangler: page")
    }

    @Test
    fun testSavingContent() {
        val content = dummyContentDto()

        val response: ResponseEntity<SaveContentResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(indexCount()).isEqualTo(11L)
        assertThat(repository.existsById("$TEAM_NAME-${content.id}")).isTrue()
    }

    @Test
    fun testSavingContentWithInvalidToken() {
        val content = dummyContentDto()

        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), authHeader(valid = false)),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun testSavingContentWithMissingId() {
        val response: ResponseEntity<ErrorResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(dummyContentDto(id = null)), authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.message).isEqualTo("id er påkrevd for alle dokumenter")
    }

    @Test
    fun testSavingContentWithMissingRequiredField() {
        val content = dummyContentDto(ingress = null)

        val response: ResponseEntity<SaveContentResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(content), authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.numberOfIndexedDocuments).isEqualTo(0)
        assertThat(response.body?.numberOfFailedDocuments).isEqualTo(1)
        assertThat(response.body!!.validationErrors[content.id]!!.first()).isEqualTo("Påkrevd felt mangler: ingress")
    }

    @Test
    fun testSavingContentWithInvalidLanguage() {
        val response: ResponseEntity<SaveContentResponse> = restTemplate.exchange(
            "${host()}/content/$TEAM_NAME",
            HttpMethod.POST,
            HttpEntity(listOf(dummyContentDto(language = "røverspråk")), authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body!!.validationErrors["11"]!!.first()).isEqualTo("Ugyldig verdi for metadata.language: røverspråk. Må være tobokstavs språkkode fra kodeverk-api.")
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
            HttpEntity(listOf(content), authHeader()),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(response.body?.message).isEqualTo("Ukjent feil")
    }

    @Test
    fun testDeletingContent() {
        val deletedId = "1"
        val response: ResponseEntity<String> =
            restTemplate.exchange(
                "${host()}/content/$TEAM_NAME/$deletedId",
                HttpMethod.DELETE,
                HttpEntity<Any>(authHeader()),
            )


        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(indexCount()).isEqualTo(9L)
        assertThat(repository.existsById("$TEAM_NAME-$deletedId")).isFalse()
    }

    @Test
    fun testDeletingContentForMissingApp() {
        val deletedId = "1"
        val teamName = "missing-team"
        val response: ResponseEntity<ErrorResponse> =
            restTemplate.exchange(
                "${host()}/content/$teamName/$deletedId",
                HttpMethod.DELETE,
                HttpEntity<Any>(authHeader()),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.message).isEqualTo("Dokument med ekstern id $deletedId finnes ikke for team $teamName")
    }
}