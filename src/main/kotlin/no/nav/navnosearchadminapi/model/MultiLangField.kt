package no.nav.navnosearchadminapi.model

import no.nav.navnosearchadminapi.utils.ENGLISH
import no.nav.navnosearchadminapi.utils.norwegianLanguageCodes
import no.nav.navnosearchadminapi.utils.supportedLanguages
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

data class MultiLangField(
    @Field(type = FieldType.Text, analyzer = "english_with_html_strip") val en: String? = null,
    @Field(type = FieldType.Text, analyzer = "norwegian_with_html_strip") val no: String? = null,
    @Field(type = FieldType.Text, analyzer = "standard_with_html_strip") val other: String? = null,
) {
    constructor(value: String, language: String) : this(
        en = if (ENGLISH == language) value else null,
        no = if (norwegianLanguageCodes.contains(language)) value else null,
        other = if (!supportedLanguages.contains(language)) value else null,
    )
}