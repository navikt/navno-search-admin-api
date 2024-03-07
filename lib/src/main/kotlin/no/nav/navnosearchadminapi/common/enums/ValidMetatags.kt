package no.nav.navnosearchadminapi.common.enums

enum class ValidMetatags(override val descriptor: String) : DescriptorProvider {
    INFORMASJON("informasjon"),
    NYHET("nyhet"),
    PRESSE("presse"),
    PRESSEMELDING("pressemelding"),
    ANALYSE("analyse"),
    STATISTIKK("statistikk"),
}