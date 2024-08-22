package no.nav.navnosearchadminapi.consumer.azuread.dto.inbound

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class TokenRequest(
    val clientId: String,
    val clientSecret: String,
    val grantType: String = CLIENT_CREDENTIALS,
    val scope: String,
) {
    fun asMultiValueMap(): MultiValueMap<String, String> {
        return LinkedMultiValueMap<String, String>().apply {
            add(CLIENT_ID, clientId)
            add(CLIENT_SECRET, clientSecret)
            add(GRANT_TYPE, grantType)
            add(SCOPE, scope)
        }
    }

    companion object {
        private const val CLIENT_ID = "client_id"
        private const val CLIENT_SECRET = "client_secret"
        private const val GRANT_TYPE = "grant_type"
        private const val SCOPE = "scope"

        private const val CLIENT_CREDENTIALS = "client_credentials"
    }
}