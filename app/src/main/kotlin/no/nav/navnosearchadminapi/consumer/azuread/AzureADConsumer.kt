package no.nav.navnosearchadminapi.consumer.azuread

import no.nav.navnosearchadminapi.consumer.azuread.dto.inbound.TokenRequest
import no.nav.navnosearchadminapi.consumer.azuread.dto.outbound.TokenResponse
import no.nav.navnosearchadminapi.exception.TokenFetchException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class AzureADConsumer(
    val restTemplate: RestTemplate,
    @param:Value("\${no.nav.security.jwt.issuer.azuread.accepted-audience}") val clientId: String,
    @param:Value("\${no.nav.security.jwt.issuer.azuread.client-secret}") val clientSecret: String,
    @param:Value("\${no.nav.security.jwt.issuer.azuread.token-endpoint}") val tokenEndpoint: String,
) {
    fun getAccessToken(scope: String): String {
        try {
            return restTemplate.exchange<TokenResponse>(
                tokenEndpoint,
                HttpMethod.POST,
                createRequestEntity(scope),
                TokenResponse::class
            ).body!!.accessToken
        } catch (e: HttpStatusCodeException) {
            throw TokenFetchException("Henting av Azure AD access token feilet. ${e.message}", e)
        }
    }

    private fun createRequestEntity(scope: String): HttpEntity<MultiValueMap<String, String>> {
        return HttpEntity(
            TokenRequest(clientId = clientId, clientSecret = clientSecret, scope = scope).asMultiValueMap(),
            HttpHeaders().apply { contentType = MediaType.APPLICATION_FORM_URLENCODED }
        )
    }
}