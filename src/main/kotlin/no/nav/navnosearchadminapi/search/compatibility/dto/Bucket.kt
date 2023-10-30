package no.nav.navnosearchadminapi.search.compatibility.dto

interface Bucket {
    val key: String
    val docCount: Long
    val checked: Boolean
}