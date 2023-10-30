package no.nav.navnosearchadminapi.admin.repository

import no.nav.navnosearchadminapi.common.model.ContentDao
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface ContentRepository : CrudRepository<ContentDao, String> {
    fun findAllByTeamOwnedBy(teamOwnedBy: String, pageable: Pageable): Page<ContentDao>
}