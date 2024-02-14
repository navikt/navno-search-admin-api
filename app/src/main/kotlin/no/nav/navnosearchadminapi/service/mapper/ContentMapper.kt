package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.NORWEGIAN
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.common.model.ContentDao
import no.nav.navnosearchadminapi.common.model.MultiLangFieldLong
import no.nav.navnosearchadminapi.common.model.MultiLangFieldShort
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import no.nav.navnosearchadminapi.utils.createInternalId
import no.nav.navnosearchadminapi.utils.listOfNotNullOrBlank
import org.jsoup.Jsoup
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.stereotype.Component

@Component
class ContentMapper {
    fun toContentDao(content: ContentDto, teamName: String): ContentDao {
        val language = content.metadata!!.language!!
        val title = content.title!!
        val ingress = removeHtmlAndMacrosFromString(content.ingress!!)
        val text = removeHtmlAndMacrosFromString(content.text!!)
        val type = content.metadata.type

        return ContentDao(
            id = createInternalId(teamName, content.id!!),
            teamOwnedBy = teamName,
            href = content.href!!,
            autocomplete = Completion(listOf(content.title)),
            title = MultiLangFieldShort(title, language),
            ingress = MultiLangFieldShort(ingress, language),
            text = MultiLangFieldLong(text, language),
            allText = MultiLangFieldLong(
                listOfNotNullOrBlank(
                    title,
                    ingress,
                    text,
                    type.takeIf { shouldBeIncludedInAllTextField(it) }
                ).joinToString(), language
            ),
            type = type,
            createdAt = content.metadata.createdAt!!,
            lastUpdated = content.metadata.lastUpdated!!,
            audience = content.metadata.audience!!,
            language = resolveLanguage(language),
            fylke = content.metadata.fylke,
            metatags = resolveMetatags(content.metadata),
            keywords = content.metadata.keywords,
            languageRefs = content.metadata.languageRefs.map { resolveLanguage(it) }
                .filter { it != resolveLanguage(language) },
        )
    }

    private fun shouldBeIncludedInAllTextField(type: String): Boolean {
        return type in listOf(ValidTypes.SKJEMA.descriptor)
    }

    private fun removeHtmlAndMacrosFromString(string: String): String {
        return Jsoup.parse(string).text().replace(Regex("\\[.*?/]"), "")
    }

    private fun resolveMetatags(metadata: ContentMetadata): List<String> {
        if (isInformasjon(metadata)) {
            return listOf(ValidMetatags.INFORMASJON.descriptor)
        }
        return metadata.metatags
    }

    private fun resolveLanguage(language: String): String {
        if (language.equals(NORWEGIAN, ignoreCase = true)) {
            return NORWEGIAN_BOKMAAL
        }
        return language.lowercase()
    }

    private fun isInformasjon(metadata: ContentMetadata): Boolean {
        return metadata.metatags.isEmpty() && metadata.fylke == null && metadata.type !in listOf(
            ValidTypes.SKJEMA.descriptor,
            ValidTypes.KONTOR.descriptor,
            ValidTypes.KONTOR_LEGACY.descriptor,
        )
    }
}