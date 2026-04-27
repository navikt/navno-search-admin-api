package no.nav.navnosearchadminapi.consumer.azuread

import no.nav.navnosearchadminapi.consumer.azuread.dto.inbound.TokenRequest
import no.nav.navnosearchadminapi.consumer.azuread.dto.outbound.TokenResponse
import no.nav.navnosearchadminapi.exception.TokenFetchException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestClient

@Component
class AzureADConsumer(
    val restClient: RestClient,
    @param:Value("\${no.nav.security.jwt.issuer.azuread.accepted-audience}") val clientId: String,
    @param:Value("\${no.nav.security.jwt.issuer.azuread.client-secret}") val clientSecret: String,
    @param:Value("\${no.nav.security.jwt.issuer.azuread.token-endpoint}") val tokenEndpoint: String,
) {
    fun getAccessToken(scope: String): String {
        try {
            return restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(TokenRequest(clientId = clientId, clientSecret = clientSecret, scope = scope).asMultiValueMap())
                .retrieve()
                .body(TokenResponse::class.java)!!
                .accessToken
        } catch (e: HttpStatusCodeException) {
            throw TokenFetchException("Henting av Azure AD access token feilet. ${e.message}", e)
        }
    }
}