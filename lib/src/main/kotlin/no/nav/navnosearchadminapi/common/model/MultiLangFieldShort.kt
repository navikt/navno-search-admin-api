package no.nav.navnosearchadminapi.common.model

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.norwegianLanguageCodes
import no.nav.navnosearchadminapi.common.model.annotations.EnglishTextField
import no.nav.navnosearchadminapi.common.model.annotations.NorwegianShortTextField
import no.nav.navnosearchadminapi.common.model.annotations.StandardTextField

// For title and ingress
data class MultiLangFieldShort(
    @field:EnglishTextField override val en: String? = null,
    @field:NorwegianShortTextField override val no: String? = null,
    @field:StandardTextField override val other: String? = null,
) : MultiLangField {
    companion object {
        fun from(value: String, language: String) = when (language) {
            ENGLISH -> MultiLangFieldShort(en = value)
            in norwegianLanguageCodes -> MultiLangFieldShort(no = value)
            else -> MultiLangFieldShort(other = value)
        }
    }
}