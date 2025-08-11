package no.nav.navnosearchadminapi.repository

import no.nav.navnosearchadminapi.common.model.Content
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface ContentRepository : CrudRepository<Content, String> {
    fun findAllByTeamOwnedBy(teamOwnedBy: String, pageable: Pageable): Page<Content>
}