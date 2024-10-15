package no.nav.navnosearchadminapi.consumer.kodeverk

import no.nav.navnosearchadminapi.consumer.azuread.AzureADConsumer
import no.nav.navnosearchadminapi.consumer.kodeverk.dto.KodeverkResponse
import no.nav.navnosearchadminapi.exception.KodeverkConsumerException
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class KodeverkConsumer(
    val restTemplate: RestTemplate,
    val azureADConsumer: AzureADConsumer,
    @Value("\${kodeverk.spraak.url}") val kodeverkUrl: String,
    @Value("\${kodeverk.scope}") val kodeverkScope: String,
) {

    @Cacheable("spraakkoder")
    fun fetchSpraakKoder(): KodeverkResponse {
        try {
            return azureADConsumer.getAccessToken(kodeverkScope).let { accessToken ->
                restTemplate.exchange(
                    kodeverkUrl,
                    HttpMethod.GET,
                    HttpEntity<Any>(headers(accessToken)),
                    KodeverkResponse::class.java
                )
            }.body ?: throw KodeverkConsumerException("Tom response body fra kodeverk-api")
        } catch (e: HttpStatusCodeException) {
            throw KodeverkConsumerException("Feil ved kall til kodeverk-api. ${e.message}", e)
        }
    }


    private fun headers(accessToken: String): HttpHeaders {
        return HttpHeaders().apply {
            setBearerAuth(accessToken)
            set(NAV_CALL_ID, UUID.randomUUID().toString())
            set(NAV_CONSUMER_ID, NAVNO_SEARCH_ADMIN_API)
        }
    }

    companion object {
        const val NAV_CALL_ID = "Nav-Call-Id"
        const val NAV_CONSUMER_ID = "Nav-Consumer-Id"

        const val NAVNO_SEARCH_ADMIN_API = "navno-search-admin-api"
    }
}