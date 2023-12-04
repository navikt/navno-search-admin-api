package no.nav.navnosearchadminapi.common.enums

enum class ValidMetatags(override val descriptor: String) : DescriptorProvider {
    INFORMASJON("informasjon"),
    NYHET("nyhet"),
    PRESSE("presse"),
    PRESSEMELDING("pressemelding"),
    NAV_OG_SAMFUNN("nav-og-samfunn"),
    ANALYSE("analyse"),
    STATISTIKK("statistikk"),
}