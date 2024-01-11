package no.nav.navnosearchadminapi.common.model

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.norwegianLanguageCodes
import no.nav.navnosearchadminapi.common.constants.supportedLanguages
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.InnerField
import org.springframework.data.elasticsearch.annotations.MultiField

/*
Ett felt per støttede språk.
Inner field med suffix "exact" brukes til match phrase queries.
 */
data class MultiLangField(
    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "custom_english"),
        otherFields = [InnerField(suffix = "exact", type = FieldType.Text, analyzer = "custom_standard")]
    ) val en: String? = null,
    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "custom_norwegian"),
        otherFields = [InnerField(suffix = "exact", type = FieldType.Text, analyzer = "custom_standard")]
    ) val no: String? = null,
    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "custom_standard"),
        otherFields = [InnerField(suffix = "exact", type = FieldType.Text, analyzer = "custom_standard")]
    ) val other: String? = null,
) {
    constructor(value: String, language: String) : this(
        en = if (ENGLISH == language) value else null,
        no = if (norwegianLanguageCodes.contains(language)) value else null,
        other = if (!supportedLanguages.contains(language)) value else null,
    )
}