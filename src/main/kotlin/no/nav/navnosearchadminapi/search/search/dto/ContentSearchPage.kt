package no.nav.navnosearchadminapi.search.search.dto

data class ContentSearchPage(
    val suggestions: List<String>?,
    val hits: List<ContentSearchHit>,
    val aggregations: ContentAggregations,
    val totalPages: Int,
    val totalElements: Long,
    val pageSize: Int,
    val pageNumber: Int
)