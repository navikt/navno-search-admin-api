package no.nav.navnosearchadminapi.common.model

interface MultiLangField {
    val en: String?
    val no: String?
    val other: String?

    val value: String
        get() = listOfNotNull(en, no, other).firstOrNull() ?: error("Fant ikke populert språkfelt")
}