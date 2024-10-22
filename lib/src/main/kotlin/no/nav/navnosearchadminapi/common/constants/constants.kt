package no.nav.navnosearchadminapi.common.constants

const val NORWEGIAN = "no"

const val NORWEGIAN_BOKMAAL = "nb"
const val NORWEGIAN_NYNORSK = "nn"
const val ENGLISH = "en"
const val OTHER = "other"

const val ID = "id"
const val HREF = "href"
const val TITLE = "title"
const val INGRESS = "ingress"
const val TEXT = "text"
const val ALL_TEXT = "allText"
const val CREATED_AT = "createdAt"
const val LAST_UPDATED = "lastUpdated"
const val SORT_BY_DATE = "sortByDate"
const val TYPE = "type"
const val AUDIENCE = "audience"
const val LANGUAGE = "language"
const val FYLKE = "fylke"
const val METATAGS = "metatags"
const val LANGUAGE_REFS = "languageRefs"

const val EXACT_INNER_FIELD = "exact"
const val NGRAMS_INNER_FIELD = "ngrams"

const val METADATA = "metadata"
const val METADATA_CREATED_AT = "$METADATA.$CREATED_AT"
const val METADATA_LAST_UPDATED = "$METADATA.$LAST_UPDATED"
const val METADATA_TYPE = "$METADATA.$TYPE"
const val METADATA_AUDIENCE = "$METADATA.$AUDIENCE"
const val METADATA_LANGUAGE = "$METADATA.$LANGUAGE"
const val METADATA_FYLKE = "$METADATA.$FYLKE"
const val METADATA_METATAGS = "$METADATA.$METATAGS"
const val METADATA_LANGUAGE_REFS = "$METADATA.$LANGUAGE_REFS"


val norwegianLanguageCodes = listOf(NORWEGIAN, NORWEGIAN_BOKMAAL, NORWEGIAN_NYNORSK)
val languageSubfields = listOf(NORWEGIAN, ENGLISH, OTHER)
