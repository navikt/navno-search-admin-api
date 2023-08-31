package no.nav.navnosearchapi.dto

data class ContentAggregations(
    val audience: Map<String, Long>,
    val language: Map<String, Long>,
    val fylke: Map<String, Long>,
    val metatags: Map<String, Long>,
    val isFile: Map<String, Long>,
    val dateRangeAggregations: Map<String, Long>
)