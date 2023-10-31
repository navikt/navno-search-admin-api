package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.model.ContentDao
import no.nav.navnosearchadminapi.common.model.MultiLangField
import no.nav.navnosearchadminapi.common.utils.ENGLISH
import no.nav.navnosearchadminapi.common.utils.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.utils.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import no.nav.navnosearchadminapi.utils.extractExternalId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ContentDtoMapper {

    val logger: Logger = LoggerFactory.getLogger(ContentDtoMapper::class.java)

    fun toContentDto(content: ContentDao): ContentDto {
        return ContentDto(
            id = extractExternalId(content.id, content.teamOwnedBy),
            href = content.href,
            title = languageSubfieldValue(content.title, content.language)
                ?: handleMissingValue(content.id, TITLE),
            ingress = languageSubfieldValue(content.ingress, content.language)
                ?: handleMissingValue(content.id, INGRESS),
            text = languageSubfieldValue(content.text, content.language)
                ?: handleMissingValue(content.id, TEXT),
            ContentMetadata(
                createdAt = content.createdAt,
                lastUpdated = content.lastUpdated,
                audience = content.audience,
                language = content.language,
                isFile = content.isFile,
                fylke = content.fylke,
                metatags = content.metatags,
                keywords = content.keywords,
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