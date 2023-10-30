package no.nav.navnosearchadminapi.utils

import no.nav.navnosearchadminapi.exception.DocumentForTeamNameNotFoundException

fun createInternalId(teamName: String, externalId: String) = "$teamName-$externalId"

fun extractExternalId(id: String, teamName: String): String {
    val expectedPrefix = "$teamName-"
    if (id.startsWith(expectedPrefix)) {
        return id.substring(expectedPrefix.length)
    } else {
        throw DocumentForTeamNameNotFoundException("Feil ved utledning av ekstern id - dokument med id $id tilhører ikke team $teamName")
    }
}