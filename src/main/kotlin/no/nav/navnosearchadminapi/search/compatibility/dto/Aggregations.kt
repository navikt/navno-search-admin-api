package no.nav.navnosearchadminapi.search.compatibility.dto

data class Aggregations(
    val fasetter: UnderAggregations,
    val tidsperiode: DateRange
)