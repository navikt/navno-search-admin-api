package no.nav.navnosearchadminapi.utils

fun listOfNotNullOrBlank(vararg elements: String?): List<String> = elements.filterNotNull().filter { it.isNotBlank() }