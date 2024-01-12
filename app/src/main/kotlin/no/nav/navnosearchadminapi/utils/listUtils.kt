package no.nav.navnosearchadminapi.utils

fun listOfNotBlank(vararg elements: String): List<String> = elements.filter { it.isNotBlank() }