package no.nav.navnosearchadminapi.consumer.azuread.dto.inbound

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenRequest(
    @JsonProperty("client_id") val clientId: String,
    @JsonProperty("client_secret") val clientSecret: String,
    @JsonProperty("grant_type") val grantType: String = CLIENT_CREDENTIALS,
    @JsonProperty("scope") val scope: String,
) {
    companion object {
        private const val CLIENT_CREDENTIALS = "client_credentials"
    }
}