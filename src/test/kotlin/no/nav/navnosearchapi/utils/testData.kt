package no.nav.navnosearchapi.utils

import no.nav.navnosearchapi.dto.ContentDto
import no.nav.navnosearchapi.model.ContentDao
import no.nav.navnosearchapi.model.MultiLangField

const val TEAM_NAME = "test-team"
const val PRIVATPERSON = "Privatperson"
const val ARBEIDSGIVER = "Arbeidsgiver"
const val SAMARBEIDSPARTNER = "Samarbeidspartner"

val initialTestData = listOf(
    ContentDao(
        "$TEAM_NAME-1",
        TEAM_NAME,
        "https://first.com",
        MultiLangField(en = "First name"),
        MultiLangField(en = "First ingress"),
        MultiLangField(en = "First text"),
        PRIVATPERSON,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-2",
        TEAM_NAME,
        "https://second.com",
        MultiLangField(en = "Second name"),
        MultiLangField(en = "Second ingress"),
        MultiLangField(en = "Second text"),
        PRIVATPERSON,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-3",
        TEAM_NAME,
        "https://third.com",
        MultiLangField(en = "Third name"),
        MultiLangField(en = "Third ingress"),
        MultiLangField(en = "Third text"),
        PRIVATPERSON,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-4",
        TEAM_NAME,
        "https://fourth.com",
        MultiLangField(en = "Fourth name"),
        MultiLangField(en = "Fourth ingress"),
        MultiLangField(en = "Fourth text"),
        PRIVATPERSON,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-5",
        TEAM_NAME,
        "https://fifth.com",
        MultiLangField(en = "Fifth name"),
        MultiLangField(en = "Fifth ingress"),
        MultiLangField(en = "Fifth text"),
        ARBEIDSGIVER,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-6",
        TEAM_NAME,
        "https://sixth.com",
        MultiLangField(en = "Sixth name"),
        MultiLangField(en = "Sixth ingress"),
        MultiLangField(en = "Sixth text"),
        ARBEIDSGIVER,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-7",
        TEAM_NAME,
        "https://seventh.com",
        MultiLangField(en = "Seventh name"),
        MultiLangField(en = "Seventh ingress"),
        MultiLangField(en = "Seventh text"),
        ARBEIDSGIVER,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-8",
        TEAM_NAME,
        "https://eighth.com",
        MultiLangField(en = "Eighth name"),
        MultiLangField(en = "Eighth ingress"),
        MultiLangField(en = "Eighth text"),
        SAMARBEIDSPARTNER,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-9",
        TEAM_NAME,
        "https://ninth.com",
        MultiLangField(en = "Ninth name"),
        MultiLangField(en = "Ninth ingress"),
        MultiLangField(en = "Ninth text"),
        SAMARBEIDSPARTNER,
        ENGLISH
    ),
    ContentDao(
        "$TEAM_NAME-10",
        TEAM_NAME,
        "https://tenth.com",
        MultiLangField(en = "Tenth name"),
        MultiLangField(en = "Tenth ingress"),
        MultiLangField(en = "Tenth text"),
        SAMARBEIDSPARTNER,
        ENGLISH
    ),
)

val additionalTestData = listOf(
    ContentDto(
        "11",
        "https://eleventh.com",
        "Eleventh name",
        "Eleventh ingress",
        "Eleventh text",
        SAMARBEIDSPARTNER,
        ENGLISH
    )
)

val additionalTestDataAsMapWithMissingIngress = listOf(
    mapOf(
        "id" to "11",
        "href" to "https://eleventh.com",
        "name" to "Eleventh name",
        "text" to "Eleventh text",
        "audience" to SAMARBEIDSPARTNER,
        "language" to ENGLISH
    )
)