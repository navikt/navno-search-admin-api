package no.nav.navnosearchadminapi.common.model

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.norwegianLanguageCodes
import no.nav.navnosearchadminapi.common.constants.supportedLanguages
import no.nav.navnosearchadminapi.common.model.annotations.EnglishTextField
import no.nav.navnosearchadminapi.common.model.annotations.NorwegianLongTextField
import no.nav.navnosearchadminapi.common.model.annotations.StandardTextField

// For longer text fields
data class MultiLangFieldLong(
    @field:EnglishTextField override val en: List<String> = emptyList(),
    @field:NorwegianLongTextField override val no: List<String> = emptyList(),
    @field:StandardTextField override val other: List<String> = emptyList(),
) : MultiLangField {
    constructor(values: List<String>, language: String) : this(
        en = if (ENGLISH == language) values else emptyList(),
        no = if (norwegianLanguageCodes.contains(language)) values else emptyList(),
        other = if (!supportedLanguages.contains(language)) values else emptyList(),
    )
}