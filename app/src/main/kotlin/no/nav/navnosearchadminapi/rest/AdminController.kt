package no.nav.navnosearchadminapi.rest

import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.outbound.SaveContentResponse
import no.nav.navnosearchadminapi.service.AdminService
import no.nav.security.token.support.spring.ProtectedRestController
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@ProtectedRestController(issuer = "azuread")
class AdminController(val service: AdminService) {

    @PostMapping("/content/{teamName}")
    fun saveContent(
        @RequestBody content: List<ContentDto>,
        @PathVariable teamName: String
    ): SaveContentResponse {
        return service.saveAllContent(content, teamName)
    }

    @GetMapping("/content/{teamName}")
    fun getContentForTeamName(
        @PathVariable teamName: String,
        @RequestParam page: Int
    ): Page<ContentDto> {
        return service.getContentForTeamName(teamName, page)
    }

    @DeleteMapping("/content/{teamName}/{id}")
    fun deleteContentByTeamNameAndId(@PathVariable teamName: String, @PathVariable id: String): String {
        service.deleteContentByTeamNameAndId(teamName, id)
        return "Dokument med id $id slettet"
    }
}