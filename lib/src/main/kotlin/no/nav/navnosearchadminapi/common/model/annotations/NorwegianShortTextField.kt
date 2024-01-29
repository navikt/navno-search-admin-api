package no.nav.navnosearchadminapi.common.model.annotations

import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.InnerField
import org.springframework.data.elasticsearch.annotations.MultiField

@MultiField(
    mainField = Field(
        type = FieldType.Text,
        analyzer = "custom_norwegian_index",
        searchAnalyzer = "custom_norwegian_search"
    ), otherFields = [
        InnerField(suffix = "exact", type = FieldType.Text, analyzer = "custom_standard"),
        InnerField(
            suffix = "ngrams", type = FieldType.Text,
            analyzer = "custom_norwegian_ngrams",
            searchAnalyzer = "custom_norwegian_search"
        )
    ]
)
@Target(AnnotationTarget.FIELD)
annotation class NorwegianShortTextField