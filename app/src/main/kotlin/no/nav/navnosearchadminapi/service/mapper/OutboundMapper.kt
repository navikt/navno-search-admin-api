package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.model.Content
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import no.nav.navnosearchadminapi.utils.extractExternalId

fun Content.toOutbound() = ContentDto(
    id = extractExternalId(id, teamOwnedBy),
    href = href,
    title = title.value,
    ingress = ingress.value,
    text = text.value,
    metadata = ContentMetadata(
        type = type,
        createdAt = createdAt,
        lastUpdated = lastUpdated,
        audience = audience,
        language = language,
        fylke = fylke,
        metatags = metatags,
        languageRefs = languageRefs,
    )
)