package no.nav.navnosearchadminapi.utils

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.enums.ValidAudiences
import no.nav.navnosearchadminapi.common.enums.ValidFylker
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.common.model.Content
import no.nav.navnosearchadminapi.common.model.MultiLangFieldLong
import no.nav.navnosearchadminapi.common.model.MultiLangFieldShort
import no.nav.navnosearchadminapi.consumer.kodeverk.dto.KodeverkResponse
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import org.springframework.data.elasticsearch.core.suggest.Completion
import java.time.ZonedDateTime

const val TEAM_NAME = "test-team"
const val HINDI = "hi"

val mockedKodeverkResponse = KodeverkResponse(listOf("NB", "NN", "EN", "SE", "PL", "UK", "RU"))

val now: ZonedDateTime = ZonedDateTime.now()
val nowMinusTwoYears: ZonedDateTime = ZonedDateTime.now().minusYears(2)
val nowMinus10Days: ZonedDateTime = ZonedDateTime.now().minusDays(10)
val nowMinus50Days: ZonedDateTime = ZonedDateTime.now().minusDays(50)

val initialTestData = listOf(
    dummyContent(
        externalId = "1",
        textPrefix = "First",
        audience = listOf(ValidAudiences.PERSON.descriptor, ValidAudiences.EMPLOYER.descriptor, ValidAudiences.PROVIDER.descriptor),
        fylke = ValidFylker.AGDER.descriptor,
        metatags = listOf(ValidMetatags.STATISTIKK.descriptor)
    ),
    dummyContent(
        externalId = "2",
        textPrefix = "Second",
        fylke = ValidFylker.AGDER.descriptor,
        metatags = listOf(ValidMetatags.STATISTIKK.descriptor)
    ),
    dummyContent(
        externalId = "3",
        textPrefix = "Third",
        timestamp = nowMinusTwoYears,
        fylke = ValidFylker.AGDER.descriptor,
        metatags = listOf(ValidMetatags.STATISTIKK.descriptor)
    ),
    dummyContent(
        externalId = "4",
        textPrefix = "Fourth",
        timestamp = nowMinusTwoYears,
        language = ENGLISH
    ),
    dummyContent(
        externalId = "5",
        textPrefix = "Fifth",
        timestamp = nowMinus10Days,
        audience = listOf(ValidAudiences.EMPLOYER.descriptor),
        language = ENGLISH,
    ),
    dummyContent(
        externalId = "6",
        textPrefix = "Sixth",
        timestamp = nowMinus10Days,
        audience = listOf(ValidAudiences.EMPLOYER.descriptor),
        language = ENGLISH,
    ),
    dummyContent(
        externalId = "7",
        textPrefix = "Seventh",
        timestamp = nowMinus50Days,
        audience = listOf(ValidAudiences.EMPLOYER.descriptor),
        language = HINDI,
    ),
    dummyContent(
        externalId = "8",
        textPrefix = "Eighth",
        timestamp = nowMinus50Days,
        audience = listOf(ValidAudiences.PROVIDER.descriptor),
        language = HINDI,
    ),
    dummyContent(
        externalId = "9",
        textPrefix = "Ninth",
        timestamp = nowMinus50Days,
        audience = listOf(ValidAudiences.PROVIDER.descriptor),
        language = HINDI,
    ),
    dummyContent(
        externalId = "10",
        textPrefix = "Tenth",
        timestamp = nowMinus50Days,
        audience = listOf(ValidAudiences.PROVIDER.descriptor),
        language = HINDI,
    ),
)

fun dummyContent(
    teamName: String = TEAM_NAME,
    externalId: String,
    textPrefix: String = "",
    timestamp: ZonedDateTime = now,
    audience: List<String> = listOf(ValidAudiences.PERSON.descriptor),
    language: String = NORWEGIAN_BOKMAAL,
    fylke: String? = null,
    metatags: List<String> = emptyList()
): Content {
    val title = "$textPrefix title"
    val ingress = "$textPrefix ingress"
    val text = "$textPrefix text"
    return Content(
        id = "$teamName-$externalId",
        autocomplete = Completion(listOf("$textPrefix title")),
        teamOwnedBy = teamName,
        href = "https://$textPrefix.com",
        title = MultiLangFieldShort(value = title, language = language),
        ingress = MultiLangFieldShort(value = ingress, language = language),
        text = MultiLangFieldLong(value = text, language = language),
        //allText = MultiLangFieldLong(value = text, language = language), todo: fix
        type = ValidTypes.ANDRE.descriptor,
        createdAt = timestamp,
        lastUpdated = timestamp,
        sortByDate = timestamp,
        audience = audience,
        language = language,
        fylke = fylke,
        metatags = metatags
    )
}

fun dummyContentDto(
    id: String? = "11",
    href: String? = "https://eleventh.com",
    title: String? = "Eleventh title",
    ingress: String? = "Eleventh ingress",
    text: String? = "Eleventh text",
    type: String = ValidTypes.ANDRE.descriptor,
    createdAt: ZonedDateTime? = now,
    lastUpdated: ZonedDateTime? = now,
    audience: List<String>? = listOf(ValidAudiences.PROVIDER.descriptor),
    language: String? = ENGLISH,
    fylke: String? = null,
    metatags: List<String> = emptyList(),
    languageRefs: List<String> = emptyList(),
) = ContentDto(
    id,
    href,
    title,
    ingress,
    text,
    ContentMetadata(
        type,
        createdAt,
        lastUpdated,
        audience,
        language,
        fylke,
        metatags,
        languageRefs,
    )
)
