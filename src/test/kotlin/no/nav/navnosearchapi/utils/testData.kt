package no.nav.navnosearchapi.utils

import no.nav.navnosearchapi.dto.ContentDto
import no.nav.navnosearchapi.dto.ContentMetadata
import no.nav.navnosearchapi.model.ContentDao
import no.nav.navnosearchapi.model.MultiLangField
import java.time.LocalDateTime
import java.time.ZonedDateTime

const val TEAM_NAME = "test-team"
const val PRIVATPERSON = "privatperson"
const val ARBEIDSGIVER = "arbeidsgiver"
const val SAMARBEIDSPARTNER = "samarbeidspartner"
const val AGDER = "agder"
const val STATISTIKK = "statistikk"

val now: ZonedDateTime = ZonedDateTime.now()
val nowMinusTwoYears: ZonedDateTime = ZonedDateTime.now().minusYears(2)
val nowMinus10Days: ZonedDateTime = ZonedDateTime.now().minusDays(10)
val nowMinus50Days: ZonedDateTime = ZonedDateTime.now().minusDays(50)

val initialTestData = listOf(
    ContentDao(
        "$TEAM_NAME-1",
        TEAM_NAME,
        "https://first.com",
        MultiLangField(en = "First name"),
        MultiLangField(en = "First ingress"),
        MultiLangField(en = "First text"),
        now,
        now,
        listOf(PRIVATPERSON, ARBEIDSGIVER, SAMARBEIDSPARTNER),
        NORWEGIAN,
        true,
        AGDER,
        listOf(STATISTIKK)
    ),
    ContentDao(
        "$TEAM_NAME-2",
        TEAM_NAME,
        "https://second.com",
        MultiLangField(en = "Second name"),
        MultiLangField(en = "Second ingress"),
        MultiLangField(en = "Second text"),
        now,
        now,
        listOf(PRIVATPERSON),
        NORWEGIAN,
        true,
        AGDER,
        listOf(STATISTIKK)
    ),
    ContentDao(
        "$TEAM_NAME-3",
        TEAM_NAME,
        "https://third.com",
        MultiLangField(en = "Third name"),
        MultiLangField(en = "Third ingress"),
        MultiLangField(en = "Third text"),
        nowMinusTwoYears,
        nowMinusTwoYears,
        listOf(PRIVATPERSON),
        NORWEGIAN,
        true,
        AGDER,
        listOf(STATISTIKK)
    ),
    ContentDao(
        "$TEAM_NAME-4",
        TEAM_NAME,
        "https://fourth.com",
        MultiLangField(en = "Fourth name"),
        MultiLangField(en = "Fourth ingress"),
        MultiLangField(en = "Fourth text"),
        nowMinusTwoYears,
        nowMinusTwoYears,
        listOf(PRIVATPERSON),
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-5",
        TEAM_NAME,
        "https://fifth.com",
        MultiLangField(en = "Fifth name"),
        MultiLangField(en = "Fifth ingress"),
        MultiLangField(en = "Fifth text"),
        nowMinus10Days,
        nowMinus10Days,
        listOf(ARBEIDSGIVER),
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-6",
        TEAM_NAME,
        "https://sixth.com",
        MultiLangField(en = "Sixth name"),
        MultiLangField(en = "Sixth ingress"),
        MultiLangField(en = "Sixth text"),
        nowMinus10Days,
        nowMinus10Days,
        listOf(ARBEIDSGIVER),
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-7",
        TEAM_NAME,
        "https://seventh.com",
        MultiLangField(en = "Seventh name"),
        MultiLangField(en = "Seventh ingress"),
        MultiLangField(en = "Seventh text"),
        nowMinus50Days,
        nowMinus50Days,
        listOf(ARBEIDSGIVER),
        OTHER
    ),
    ContentDao(
        "$TEAM_NAME-8",
        TEAM_NAME,
        "https://eighth.com",
        MultiLangField(en = "Eighth name"),
        MultiLangField(en = "Eighth ingress"),
        MultiLangField(en = "Eighth text"),
        nowMinus50Days,
        nowMinus50Days,
        listOf(SAMARBEIDSPARTNER),
        OTHER
    ),
    ContentDao(
        "$TEAM_NAME-9",
        TEAM_NAME,
        "https://ninth.com",
        MultiLangField(en = "Ninth name"),
        MultiLangField(en = "Ninth ingress"),
        MultiLangField(en = "Ninth text"),
        nowMinus50Days,
        nowMinus50Days,
        listOf(SAMARBEIDSPARTNER),
        OTHER
    ),
    ContentDao(
        "$TEAM_NAME-10",
        TEAM_NAME,
        "https://tenth.com",
        MultiLangField(en = "Tenth name"),
        MultiLangField(en = "Tenth ingress"),
        MultiLangField(en = "Tenth text"),
        nowMinus50Days,
        nowMinus50Days,
        listOf(SAMARBEIDSPARTNER),
        OTHER
    ),
)

val additionalTestData = listOf(dummyContentDto())

val additionalTestDataAsMapWithMissingIngress = listOf(
    mapOf(
        "id" to "11",
        "href" to "https://eleventh.com",
        "title" to "Eleventh name",
        "text" to "Eleventh text",
        "audience" to listOf(SAMARBEIDSPARTNER),
        "language" to ENGLISH
    )
)

fun dummyContentDto(
    id: String = "11",
    href: String = "https://eleventh.com",
    title: String = "Eleventh name",
    ingress: String = "Eleventh ingress",
    text: String = "Eleventh text",
    createdAt: LocalDateTime = now.toLocalDateTime(),
    lastUpdated: LocalDateTime = now.toLocalDateTime(),
    audience: List<String> = listOf(SAMARBEIDSPARTNER),
    language: String = ENGLISH,
    isFile: Boolean? = null,
    fylke: String? = null,
    metatags: List<String>? = null,
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