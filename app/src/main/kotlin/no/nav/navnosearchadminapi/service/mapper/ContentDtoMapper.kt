package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.common.model.Content
import no.nav.navnosearchadminapi.common.model.MultiLangField
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import no.nav.navnosearchadminapi.utils.extractExternalId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ContentDtoMapper {

    val logger: Logger = LoggerFactory.getLogger(ContentDtoMapper::class.java)

    fun toContentDto(content: Content) = with(content) {
        ContentDto(
            id = extractExternalId(id, teamOwnedBy),
            href = href,
            title = languageSubfieldValue(title, language)
                ?: handleMissingValue(id, TITLE),
            ingress = languageSubfieldValue(ingress, language)
                ?: handleMissingValue(id, INGRESS),
            text = languageSubfieldValue(text, language)
                ?: handleMissingValue(id, TEXT),
            ContentMetadata(
                type = type,
                createdAt = createdAt,
                lastUpdated = lastUpdated,
                audience = audience,
                language = language,
                fylke = fylke,
                metatags = metatags,
                keywords = keywords,
                languageRefs = languageRefs,
            )
        )
    }

    private fun languageSubfieldValue(field: MultiLangField, language: String): String? {
        return when (language) {
            NORWEGIAN_BOKMAAL, NORWEGIAN_NYNORSK -> field.no
            ENGLISH -> field.en
            else -> field.other
        }
    }

    private fun handleMissingValue(id: String, field: String): String {
        logger.warn("Mapping av felt $field feilet for dokument med id $id. Returnerer tom string.")
        return ""
    }

    companion object {
        private const val TITLE = "title"
        private const val INGRESS = "ingress"
        private const val TEXT = "text"
    }
}