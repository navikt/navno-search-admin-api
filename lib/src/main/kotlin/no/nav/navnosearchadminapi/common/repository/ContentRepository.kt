package no.nav.navnosearchadminapi.common.repository

import no.nav.navnosearchadminapi.common.model.Content
import org.springframework.data.repository.CrudRepository

interface ContentRepositoryLib : CrudRepository<Content, String> {
}