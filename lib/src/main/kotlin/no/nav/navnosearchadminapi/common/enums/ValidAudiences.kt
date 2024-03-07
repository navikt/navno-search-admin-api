package no.nav.navnosearchadminapi.common.enums

enum class ValidAudiences(override val descriptor: String) : DescriptorProvider {
    PERSON("person"),
    EMPLOYER("employer"),
    PROVIDER("provider"),
    PROVIDER_DOCTOR("provider_doctor"),
    PROVIDER_MUNICIPALITY_EMPLOYED("provider_municipality_employed"),
    PROVIDER_OPTICIAN("provider_optician"),
    PROVIDER_ADMINISTRATOR("provider_administrator"),
    PROVIDER_MEASURES_ORGANIZER("provider_measures_organizer"),
    PROVIDER_AID_SUPPLIER("provider_aid_supplier"),
    PROVIDER_OTHER("provider_other"),
    OTHER("other"),
}