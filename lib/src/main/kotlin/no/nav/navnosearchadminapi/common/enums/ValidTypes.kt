package no.nav.navnosearchadminapi.common.enums

enum class ValidTypes(override val descriptor: String) : DescriptorProvider {
    PRODUKTSIDE("produktside"),
    TEMASIDE("temaside"),
    SITUASJONSSIDE("situasjonsside"),
    SLIK_GJOR_DU_DET("slik gjør du det"),
    DEFAULT("default")
}