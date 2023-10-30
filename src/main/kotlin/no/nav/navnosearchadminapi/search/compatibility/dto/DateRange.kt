package no.nav.navnosearchadminapi.search.compatibility.dto

data class DateRange(
    val docCount: Long,
    val checked: Boolean,
    val buckets: List<DateRangeBucket>,
)