package no.nav.navnosearchadminapi.utils

val NORWEGIAN = "no"
val NORWEGIAN_BOKMAAL = "nb"
val NORWEGIAN_NYNORSK = "nn"
val ENGLISH = "en"

val norwegianLanguageCodes = listOf(NORWEGIAN, NORWEGIAN_BOKMAAL, NORWEGIAN_NYNORSK)
val supportedLanguages = norwegianLanguageCodes + ENGLISH

const val ID = "id"
const val HREF = "href"
const val TITLE = "title"
const val INGRESS = "ingress"
const val TEXT = "text"

const val METADATA = "metadata"
const val METADATA_CREATED_AT = "metadata.createdAt"
const val METADATA_LAST_UPDATED = "metadata.lastUpdated"
const val METADATA_AUDIENCE = "metadata.audience"
const val METADATA_LANGUAGE = "metadata.language"
const val METADATA_FYLKE = "metadata.fylke"
const val METADATA_METATAGS = "metadata.metatags"