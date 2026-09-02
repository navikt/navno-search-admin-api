package no.nav.navnosearchadminapi.rest

import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.outbound.SaveContentResponse
import no.nav.navnosearchadminapi.rest.aspect.ApiKeyProtected
import no.nav.navnosearchadminapi.service.AdminService
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// During a transition period, Access control is handled manually per-request in HeaderCheckAspect (api-key (internal ingress)
// or Entra ID token (external ingress). All endpoints are marked @Unprotected to opt out of token-support's
// automatic annotation-based enforcement.
@Unprotected
@RestController
class AdminController(val service: AdminService) {

    @PostMapping("/content/{teamName}")
    @ApiKeyProtected
    fun saveContent(
        @RequestBody content: List<ContentDto>,
        @PathVariable teamName: String,
        @RequestHeader(value = "xp-origin", required = false) xpOrigin: String?,
    ): SaveContentResponse {
        return service.saveAllContent(content, teamName, xpOrigin)
    }

    @GetMapping("/content/{teamName}")
    @ApiKeyProtected
    fun getContentForTeamName(
        @PathVariable teamName: String,
        @RequestParam page: Int
    ): Page<ContentDto> {
        return service.getContentForTeamName(teamName, page)
    }

    @DeleteMapping("/content/{teamName}/{id}")
    @ApiKeyProtected
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteContentByTeamNameAndId(@PathVariable teamName: String, @PathVariable id: String) {
        service.deleteContentByTeamNameAndId(teamName, id)
    }
}