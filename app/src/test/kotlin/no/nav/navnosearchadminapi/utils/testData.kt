package no.nav.navnosearchadminapi.utils

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.model.ContentDao
import no.nav.navnosearchadminapi.common.model.MultiLangField
import no.nav.navnosearchadminapi.consumer.kodeverk.dto.KodeverkResponse
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.inbound.ContentMetadata
import org.springframework.data.elasticsearch.core.suggest.Completion
import java.time.ZonedDateTime

const val TEAM_NAME = "test-team"
const val PRIVATPERSON = "privatperson"
const val ARBEIDSGIVER = "arbeidsgiver"
const val SAMARBEIDSPARTNER = "samarbeidspartner"
const val AGDER = "agder"
const val STATISTIKK = "statistikk"
const val HINDI = "hi"

val mockedKodeverkResponse = KodeverkResponse(listOf("NB", "NN", "EN", "SE", "PL", "UK", "RU"))

val now: ZonedDateTime = ZonedDateTime.now()
val nowMinusTwoYears: ZonedDateTime = ZonedDateTime.now().minusYears(2)
val nowMinus10Days: ZonedDateTime = ZonedDateTime.now().minusDays(10)
val nowMinus50Days: ZonedDateTime = ZonedDateTime.now().minusDays(50)

val initialTestData = listOf(
    dummyContentDao(
        externalId = "1",
        textPrefix = "First",
        audience = listOf(PRIVATPERSON, ARBEIDSGIVER, SAMARBEIDSPARTNER),
        isFile = true,
        fylke = AGDER,
        metatags = listOf(STATISTIKK)
    ),
    dummyContentDao(
        externalId = "2",
        textPrefix = "Second",
        isFile = true,
        fylke = AGDER,
        metatags = listOf(STATISTIKK)
    ),
    dummyContentDao(
        externalId = "3",
        textPrefix = "Third",
        timestamp = nowMinusTwoYears,
        isFile = true,
        fylke = AGDER,
        metatags = listOf(STATISTIKK)
    ),
    dummyContentDao(
        externalId = "4",
        textPrefix = "Fourth",
        timestamp = nowMinusTwoYears,
        language = ENGLISH
    ),
    dummyContentDao(
        externalId = "5",
        textPrefix = "Fifth",
        timestamp = nowMinus10Days,
        audience = listOf(ARBEIDSGIVER),
        language = ENGLISH,
    ),
    dummyContentDao(
        externalId = "6",
        textPrefix = "Sixth",
        timestamp = nowMinus10Days,
        audience = listOf(ARBEIDSGIVER),
        language = ENGLISH,
    ),
    dummyContentDao(
        externalId = "7",
        textPrefix = "Seventh",
        timestamp = nowMinus50Days,
        audience = listOf(ARBEIDSGIVER),
        language = HINDI,
    ),
    dummyContentDao(
        externalId = "8",
        textPrefix = "Eighth",
        timestamp = nowMinus50Days,
        audience = listOf(SAMARBEIDSPARTNER),
        language = HINDI,
    ),
    dummyContentDao(
        externalId = "9",
        textPrefix = "Ninth",
        timestamp = nowMinus50Days,
        audience = listOf(SAMARBEIDSPARTNER),
        language = HINDI,
    ),
    dummyContentDao(
        externalId = "10",
        textPrefix = "Tenth",
        timestamp = nowMinus50Days,
        audience = listOf(SAMARBEIDSPARTNER),
        language = HINDI,
    ),
)

fun dummyContentDao(
    teamName: String = TEAM_NAME,
    externalId: String,
    textPrefix: String,
    timestamp: ZonedDateTime = now,
    audience: List<String> = listOf(PRIVATPERSON),
    language: String = NORWEGIAN_BOKMAAL,
    isFile: Boolean = false,
    fylke: String? = null,
    metatags: List<String> = emptyList()
): ContentDao {
    return ContentDao(
        "$teamName-$externalId",
        Completion(listOf("$textPrefix title")),
        teamName,
        "https://$textPrefix.com",
        MultiLangField(value = "$textPrefix title", language = language),
        MultiLangField(value = "$textPrefix ingress", language = language),
        MultiLangField(value = "$textPrefix text", language = language),
        timestamp,
        timestamp,
        audience,
        language,
        isFile,
        fylke,
        metatags
    )
}

fun dummyContentDto(
    id: String? = "11",
    href: String? = "https://eleventh.com",
    title: String? = "Eleventh title",
    ingress: String? = "Eleventh ingress",
    text: String? = "Eleventh text",
    createdAt: ZonedDateTime? = now,
    lastUpdated: ZonedDateTime? = now,
    audience: List<String>? = listOf(SAMARBEIDSPARTNER),
    language: String? = ENGLISH,
    isFile: Boolean = false,
    fylke: String? = null,
    metatags: List<String> = emptyList(),
) = ContentDto(
    id,
    href,
    title,
    ingress,
    text,
    ContentMetadata(
        createdAt,
        lastUpdated,
        audience,
        language,
        isFile,
        fylke,
        metatags,
    )
)