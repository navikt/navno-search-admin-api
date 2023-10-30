package no.nav.navnosearchadminapi.search.compatibility.dto

data class UnderAggregations(
    val buckets: List<FacetBucket>? = emptyList(),
)