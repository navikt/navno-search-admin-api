package no.nav.navnosearchadminapi.consumer.azuread.dto.outbound

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponse(
    @JsonProperty("access_token") val accessToken: String,
)