package no.nav.navnosearchadminapi.rest.aspect

import jakarta.servlet.http.HttpServletRequest
import no.nav.navnosearchadminapi.exception.InvalidApiKeyException
import no.nav.navnosearchadminapi.exception.InvalidTokenException
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class HeaderCheckAspect(
    @param:Value("\${api-key}") val apiKey: String,
    val tokenValidationContextHolder: TokenValidationContextHolder,
) {

    @Before("@annotation(apiKeyProtected)")
    fun checkHeader(apiKeyProtected: ApiKeyProtected) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

        // The ekstern ingress requires a validated Entra ID token, while the intern ingress
        // (and any other, e.g. local/test) continues to use the shared api-key secret.
        if (isExternalIngress(request)) {
            validateToken()
        } else {
            validateApiKey(request)
        }
    }

    private fun isExternalIngress(request: HttpServletRequest): Boolean =
        request.serverName.contains(".ekstern.")

    private fun validateApiKey(request: HttpServletRequest) {
        val actualValue = request.getHeader(API_KEY_HEADER)

        if (actualValue != apiKey) {
            throw InvalidApiKeyException()
        }
    }

    private fun validateToken() {
        if (!tokenValidationContextHolder.getTokenValidationContext().hasTokenFor(AZURE_AD_ISSUER)) {
            throw InvalidTokenException()
        }
    }

    companion object {
        const val API_KEY_HEADER = "api-key"
        const val AZURE_AD_ISSUER = "azuread"
    }
}