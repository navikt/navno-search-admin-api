package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.constants.norwegianLanguageCodes
import no.nav.navnosearchadminapi.common.constants.supportedLanguages
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.common.model.ContentDao
import no.nav.navnosearchadminapi.common.model.MultiLangField
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import no.nav.navnosearchadminapi.utils.createInternalId
import org.jsoup.Jsoup
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.stereotype.Component

@Component
class ContentMapper {
    fun toContentDao(content: ContentDto, teamName: String): ContentDao {
        return ContentDao(
            id = createInternalId(teamName, content.id!!),
            teamOwnedBy = teamName,
            href = content.href!!,
            autocomplete = Completion(listOf(content.title)),
            title = toMultiLangField(content.title!!, content.metadata!!.language!!),
            ingress = toMultiLangField(removeHtmlAndMacrosFromString(content.ingress!!), content.metadata.language!!),
            text = toMultiLangField(removeHtmlAndMacrosFromString(content.text!!), content.metadata.language),
            type = content.metadata.type,
            createdAt = content.metadata.createdAt!!,
            lastUpdated = content.metadata.lastUpdated!!,
            audience = content.metadata.audience!!,
            language = resolveLanguage(content.metadata.language),
            fylke = content.metadata.fylke,
            metatags = resolveMetatags(content.metadata),
            keywords = content.metadata.keywords,
            languageRefs = content.metadata.languageRefs,
        )
    }

    fun removeHtmlAndMacrosFromString(string: String): String {
        return Jsoup.parse(string).text().replace(Regex("\\[.*?/]"), "")
    }

    fun resolveMetatags(metadata: ContentMetadata): List<String> {
        if (isInformasjon(metadata)) {
            return listOf(ValidMetatags.INFORMASJON.descriptor)
        }
        return metadata.metatags
    }

    fun resolveLanguage(language: String): String {
        if (language.equals(NORWEGIAN, ignoreCase = true)) {
            return NORWEGIAN_BOKMAAL
        }
        return language.lowercase()
    }

    fun toMultiLangField(value: String, language: String): MultiLangField {
        return MultiLangField(
            en = if (ENGLISH == language) value else null,
            no = if (norwegianLanguageCodes.contains(language)) value else null,
            other = if (!supportedLanguages.contains(language)) value else null,
        )
    }

    private fun isInformasjon(metadata: ContentMetadata): Boolean {
        return metadata.metatags.isEmpty() && metadata.fylke == null && metadata.type !in listOf(
            ValidTypes.SKJEMA.descriptor,
            ValidTypes.KONTOR.descriptor,
            ValidTypes.KONTOR_LEGACY.descriptor,
            ValidTypes.FIL_DOCUMENT.descriptor,
            ValidTypes.FIL_SPREADSHEET.descriptor,
            ValidTypes.FIL_ANDRE.descriptor,
        )
    }
}