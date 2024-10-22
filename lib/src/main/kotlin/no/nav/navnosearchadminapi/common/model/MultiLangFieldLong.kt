package no.nav.navnosearchadminapi.common.model

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.norwegianLanguageCodes
import no.nav.navnosearchadminapi.common.model.annotations.EnglishTextField
import no.nav.navnosearchadminapi.common.model.annotations.NorwegianLongTextField
import no.nav.navnosearchadminapi.common.model.annotations.StandardTextField

// For longer text fields
data class MultiLangFieldLong(
    @field:EnglishTextField override val en: String? = null,
    @field:NorwegianLongTextField override val no: String? = null,
    @field:StandardTextField override val other: String? = null,
) : MultiLangField {
    companion object {
        fun create(value: String, language: String) = when (language) {
            ENGLISH -> MultiLangFieldLong(en = value)
            in norwegianLanguageCodes -> MultiLangFieldLong(no = value)
            else -> MultiLangFieldLong(other = value)
        }
    }
}