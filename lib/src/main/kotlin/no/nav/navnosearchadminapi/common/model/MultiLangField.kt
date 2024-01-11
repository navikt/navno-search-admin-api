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
    ) val en: List<String> = emptyList(),
    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "custom_norwegian"),
        otherFields = [InnerField(suffix = "exact", type = FieldType.Text, analyzer = "custom_standard")]
    ) val no: List<String> = emptyList(),
    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "custom_standard"),
        otherFields = [InnerField(suffix = "exact", type = FieldType.Text, analyzer = "custom_standard")]
    ) val other: List<String> = emptyList(),
) {
    constructor(values: List<String>, language: String) : this(
        en = if (ENGLISH == language) values else emptyList(),
        no = if (norwegianLanguageCodes.contains(language)) values else emptyList(),
        other = if (!supportedLanguages.contains(language)) values else emptyList(),
    )
}