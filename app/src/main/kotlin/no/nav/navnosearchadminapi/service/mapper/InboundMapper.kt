package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.NORWEGIAN
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.common.model.Content
import no.nav.navnosearchadminapi.common.model.MultiLangFieldLong
import no.nav.navnosearchadminapi.common.model.MultiLangFieldShort
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import no.nav.navnosearchadminapi.utils.createInternalId
import no.nav.navnosearchadminapi.utils.listOfNotNullOrBlank
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("InboundMapper")

private const val MACROS_PATTERN = """\[.*?]"""

fun ContentDto.toInbound(teamName: String): Content {
    // todo: sjekke om jeg kan gjøre noe med disse feltene som er nullable pga validering men som bør være non-null
    val language = metadata!!.language!!
    val title = title!!
    val ingress = removeHtmlAndMacrosFromString(ingress!!)
    val text = removeHtmlAndMacrosFromString(text!!)
    val type = metadata.type
    val createdAt = metadata.createdAt!!
    val lastUpdated = metadata.lastUpdated!!

    return Content(
        id = createInternalId(teamName, id!!),
        teamOwnedBy = teamName,
        href = href!!,
        title = MultiLangFieldShort(title, language),
        ingress = MultiLangFieldShort(ingress, language),
        text = MultiLangFieldLong(text, language),
        allText = MultiLangFieldLong(joinTextFields(title, ingress, text, type), language),
        type = type,
        createdAt = createdAt,
        lastUpdated = lastUpdated,
        sortByDate = if (isNyhet(metadata.metatags)) createdAt else lastUpdated,
        audience = metadata.audience!!,
        language = resolveLanguage(language),
        fylke = metadata.fylke,
        metatags = resolveMetatags(metadata, id),
        languageRefs = metadata.languageRefs.map { resolveLanguage(it) }.filter { it != resolveLanguage(language) },
    )
}

private fun joinTextFields(title: String, ingress: String, text: String, type: String): String {
    return listOfNotNullOrBlank(
        title,
        ingress,
        text,
        type.takeIf { shouldBeIncludedInAllTextField(it) }
    ).joinToString()
}

private fun shouldBeIncludedInAllTextField(type: String) = type in listOf(ValidTypes.SKJEMA.descriptor)

private fun removeHtmlAndMacrosFromString(string: String): String {
    // Må parses to ganger av Jsoup av ukjent årsak
    return Jsoup.parse(string).text()
        .replace(Regex(MACROS_PATTERN), "")
        .let { Jsoup.parse(it).text() }
}

private fun resolveMetatags(metadata: ContentMetadata, id: String): List<String> {
    return if (isInformasjon(metadata)) {
        logger.info("Setter default metatag informasjon for dokument med id $id")
        listOf(ValidMetatags.INFORMASJON.descriptor)
    } else {
        metadata.metatags
    }
}

private fun resolveLanguage(language: String) = when {
    language.equals(NORWEGIAN, ignoreCase = true) -> NORWEGIAN_BOKMAAL
    else -> language.lowercase()
}

private fun isInformasjon(metadata: ContentMetadata): Boolean {
    return metadata.metatags.isEmpty() && metadata.fylke == null && metadata.type !in listOf(
        ValidTypes.SKJEMA.descriptor,
        ValidTypes.SKJEMAOVERSIKT.descriptor,
        ValidTypes.KONTOR.descriptor,
        ValidTypes.KONTOR_LEGACY.descriptor,
    )
}

private fun isNyhet(metatags: List<String>) = metatags.contains(ValidMetatags.NYHET.descriptor)