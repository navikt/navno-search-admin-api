package no.nav.navnosearchadminapi.common.enums

enum class ValidTypes(override val descriptor: String) : DescriptorProvider {
    LEGACY("legacy"),
    KONTOR_LEGACY("kontor-legacy"),
    KONTOR("kontor"),
    TABELL("tabell"),
    SKJEMA("skjema"),
    PRODUKTSIDE("produktside"),
    TEMASIDE("temaside"),
    GUIDE("guide"),
    AKTUELT("aktuelt"),
    SITUASJONSSIDE("situasjonsside"),
    OVERSIKT("oversikt"),
    SKJEMAOVERSIKT("skjemaoversikt"),
    FIL_SPREADSHEET("fil-spreadsheet"),
    FIL_DOCUMENT("fil-document"),
    FIL_ANDRE("fil-andre"),
    ANDRE("andre"),
}