package no.nav.navnosearchadminapi.common.model

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.norwegianLanguageCodes
import no.nav.navnosearchadminapi.common.constants.supportedLanguages
import no.nav.navnosearchadminapi.common.model.annotations.EnglishTextField
import no.nav.navnosearchadminapi.common.model.annotations.NorwegianShortTextField
import no.nav.navnosearchadminapi.common.model.annotations.StandardTextField

// For title and ingress
data class MultiLangFieldShort(
    @field:EnglishTextField override val en: String? = null,
    @field:NorwegianShortTextField override val no: String? = null,
    @field:StandardTextField override val other: String? = null,
) : MultiLangField {
    constructor(value: String, language: String) : this(
        en = if (ENGLISH == language) value else null,
        no = if (norwegianLanguageCodes.contains(language)) value else null,
        other = if (!supportedLanguages.contains(language)) value else null,
    )
}