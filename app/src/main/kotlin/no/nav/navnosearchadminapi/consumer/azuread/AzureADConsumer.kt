package no.nav.navnosearchadminapi.consumer.azuread

import no.nav.navnosearchadminapi.consumer.azuread.dto.inbound.TokenRequest
import no.nav.navnosearchadminapi.consumer.azuread.dto.outbound.TokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class AzureADConsumer(
    val restTemplate: RestTemplate,
    @Value("\${no.nav.security.jwt.issuer.azuread.accepted-audience}") val clientId: String,
    @Value("\${no.nav.security.jwt.issuer.azuread.client-secret}") val clientSecret: String,
    @Value("\${no.nav.security.jwt.issuer.azuread.token-endpoint}") val tokenEndpoint: String,
) {
    fun getAccessToken(scope: String): String {
        return restTemplate.exchange<TokenResponse>(
            tokenEndpoint,
            HttpMethod.POST,
            HttpEntity(
                TokenRequest(clientId = clientId, clientSecret = clientSecret, scope = scope),
                HttpHeaders().apply { contentType = MediaType.APPLICATION_FORM_URLENCODED }),
            TokenResponse::class
        ).let { response ->
            if (response.statusCode.is2xxSuccessful) {
                response.body!!.accessToken
            } else {
                throw RuntimeException()
            }
        }
    }
}