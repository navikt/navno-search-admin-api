package no.nav.navnosearchadminapi.common.constants

val NORWEGIAN = "no"
val NORWEGIAN_BOKMAAL = "nb"
val NORWEGIAN_NYNORSK = "nn"
val ENGLISH = "en"
val OTHER = "other"

val norwegianLanguageCodes = listOf(NORWEGIAN, NORWEGIAN_BOKMAAL, NORWEGIAN_NYNORSK)
val supportedLanguages = norwegianLanguageCodes + ENGLISH
val languageSubfields = listOf(NORWEGIAN, ENGLISH, OTHER)

const val ID = "id"
const val HREF = "href"
const val TITLE = "title"
const val INGRESS = "ingress"
const val TEXT = "text"
const val TITLE_WITH_SYNONYMS = "titleWithSynonyms"
const val INGRESS_WITH_SYNONYMS = "ingressWithSynonyms"
const val ALL_TEXT = "allText"
const val CREATED_AT = "createdAt"
const val LAST_UPDATED = "lastUpdated"
const val TYPE = "type"
const val AUDIENCE = "audience"
const val LANGUAGE = "language"
const val FYLKE = "fylke"
const val METATAGS = "metatags"
const val KEYWORDS = "keywords"
const val LANGUAGE_REFS = "languageRefs"

const val AUTOCOMPLETE = "autocomplete"

const val METADATA = "metadata"
const val METADATA_CREATED_AT = "$METADATA.$CREATED_AT"
const val METADATA_LAST_UPDATED = "$METADATA.$LAST_UPDATED"
const val METADATA_TYPE = "$METADATA.$TYPE"
const val METADATA_AUDIENCE = "$METADATA.$AUDIENCE"
const val METADATA_LANGUAGE = "$METADATA.$LANGUAGE"
const val METADATA_FYLKE = "$METADATA.$FYLKE"
const val METADATA_METATAGS = "$METADATA.$METATAGS"
const val METADATA_KEYWORDS = "$METADATA.$KEYWORDS"
const val METADATA_LANGUAGE_REFS = "$METADATA.$LANGUAGE_REFS"

const val TITLE_WILDCARD = "$TITLE.*"
const val INGRESS_WILDCARD = "$INGRESS.*"
const val TEXT_WILDCARD = "$TEXT.*"
const val ALL_TEXT_WILDCARD = "$ALL_TEXT.*"

const val TOTAL_COUNT = "Total count"

const val LAST_UPDATED_FROM = "lastUpdatedFrom"
const val LAST_UPDATED_TO = "lastUpdatedTo"
const val DATE_RANGE_LAST_7_DAYS = "Siste 7 dager"
const val DATE_RANGE_LAST_30_DAYS = "Siste 30 dager"
const val DATE_RANGE_LAST_12_MONTHS = "Siste 12 måneder"
const val DATE_RANGE_OLDER_THAN_12_MONTHS = "Eldre enn 12 måneder"

const val MISSING_FYLKE = "Uten fylke"