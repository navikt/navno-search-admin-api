package no.nav.navnosearchadminapi.common.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Dynamic
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting
import org.springframework.data.elasticsearch.annotations.WriteTypeHint
import java.time.ZonedDateTime

@Document(
    indexName = "search-content-v8",
    dynamic = Dynamic.STRICT,
    /* Disabler type hints da det lager et _class-felt i mappingen som gir problemer for wildcard-søk.
       Bør skrives om dersom vi trenger polymorfisk data. */
    writeTypeHint = WriteTypeHint.FALSE,
)
@Setting(settingPath = "opensearch/index-settings.json")
data class Content(
    @Id @Field(type = FieldType.Keyword) val id: String,
    @Field(type = FieldType.Keyword) val teamOwnedBy: String,
    @Field(type = FieldType.Keyword) val href: String,
    @Field(type = FieldType.Object) val title: MultiLangFieldShort,
    @Field(type = FieldType.Object) val ingress: MultiLangFieldShort,
    @Field(type = FieldType.Object) val text: MultiLangFieldLong,
    @Field(type = FieldType.Object) val allText: MultiLangFieldLong,
    @Field(type = FieldType.Keyword) val type: String,
    @Field(type = FieldType.Date) val createdAt: ZonedDateTime,
    @Field(type = FieldType.Date) val lastUpdated: ZonedDateTime,
    @Field(type = FieldType.Date) val sortByDate: ZonedDateTime,
    @Field(type = FieldType.Keyword) val audience: List<String>,
    @Field(type = FieldType.Keyword) val language: String,
    @Field(type = FieldType.Keyword) val fylke: String? = null,
    @Field(type = FieldType.Keyword) val metatags: List<String>,
    @Field(type = FieldType.Keyword) val languageRefs: List<String> = emptyList(),
) {
    companion object {
        fun from(
            id: String,
            teamOwnedBy: String,
            href: String,
            title: String,
            ingress: String,
            text: String,
            type: String,
            createdAt: ZonedDateTime,
            lastUpdated: ZonedDateTime,
            sortByDate: ZonedDateTime,
            audience: List<String>,
            language: String,
            fylke: String? = null,
            metatags: List<String>,
            languageRefs: List<String> = emptyList(),
            includeTypeInAllText: Boolean = false,
        ) = Content(
            id = id,
            teamOwnedBy = teamOwnedBy,
            href = href,
            title = MultiLangFieldShort.from(title, language),
            ingress = MultiLangFieldShort.from(ingress, language),
            text = MultiLangFieldLong.from(text, language),
            allText = MultiLangFieldLong.from(
                value = listOfNotNull(title, ingress, text, type.takeIf { includeTypeInAllText }).joinToString(),
                language = language
            ),
            type = type,
            createdAt = createdAt,
            lastUpdated = lastUpdated,
            sortByDate = sortByDate,
            audience = audience,
            language = language,
            fylke = fylke,
            metatags = metatags,
            languageRefs = languageRefs,
        )
    }
}