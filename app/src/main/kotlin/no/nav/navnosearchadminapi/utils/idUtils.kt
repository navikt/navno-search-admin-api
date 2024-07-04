package no.nav.navnosearchadminapi.utils

import no.nav.navnosearchadminapi.exception.DocumentForTeamNameNotFoundException

fun createInternalId(teamName: String, externalId: String) = "$teamName-$externalId"

fun extractExternalId(id: String, teamName: String): String {
    return "$teamName-".let { expectedPrefix ->
        if (id.startsWith(expectedPrefix)) {
            id.removePrefix(expectedPrefix)
        } else {
            throw DocumentForTeamNameNotFoundException("Feil ved utledning av ekstern id - dokument med id $id tilhører ikke team $teamName")
        }
    }
}