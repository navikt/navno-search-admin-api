package no.nav.navnosearchadminapi.service.validation

import no.nav.navnosearchadminapi.common.constants.HREF
import no.nav.navnosearchadminapi.common.constants.ID
import no.nav.navnosearchadminapi.common.constants.INGRESS
import no.nav.navnosearchadminapi.common.constants.METADATA
import no.nav.navnosearchadminapi.common.constants.METADATA_AUDIENCE
import no.nav.navnosearchadminapi.common.constants.METADATA_CREATED_AT
import no.nav.navnosearchadminapi.common.constants.METADATA_FYLKE
import no.nav.navnosearchadminapi.common.constants.METADATA_LANGUAGE
import no.nav.navnosearchadminapi.common.constants.METADATA_LANGUAGE_REFS
import no.nav.navnosearchadminapi.common.constants.METADATA_LAST_UPDATED
import no.nav.navnosearchadminapi.common.constants.METADATA_METATAGS
import no.nav.navnosearchadminapi.common.constants.METADATA_TYPE
import no.nav.navnosearchadminapi.common.constants.TEXT
import no.nav.navnosearchadminapi.common.constants.TITLE
import no.nav.navnosearchadminapi.common.enums.DescriptorProvider
import no.nav.navnosearchadminapi.common.enums.ValidAudiences
import no.nav.navnosearchadminapi.common.enums.ValidFylker
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.consumer.kodeverk.KodeverkConsumer
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.exception.MissingIdException
import no.nav.navnosearchadminapi.utils.enumContains
import no.nav.navnosearchadminapi.utils.enumDescriptors
import org.springframework.stereotype.Component

@Component
class ContentDtoValidator(val kodeverkConsumer: KodeverkConsumer) {

    fun validate(content: List<ContentDto>): Map<String, List<String>> {
        return buildMap {
            content.forEach {
                validate(it).let { validationErrors ->
                    if (validationErrors.isNotEmpty()) put(it.id ?: throw MissingIdException(), validationErrors)
                }
            }
        }
    }

    private fun validate(content: ContentDto): List<String> {
        return buildList {
            addAll(validateNotNull(requiredFieldsMap(content)))

            content.metadata?.type?.let { type -> addAll(validateType(type)) }
            content.metadata?.audience?.let { audience -> addAll(validateAudience(audience)) }
            content.metadata?.language?.let { language -> addAll(validateLanguage(language, METADATA_LANGUAGE)) }
            content.metadata?.fylke?.let { fylke -> addAll(validateFylke(fylke)) }
            content.metadata?.metatags?.let { metatags -> addAll(validateMetatags(metatags)) }
            content.metadata?.languageRefs?.let { languageRefs -> addAll(validateLanguageRefs(languageRefs)) }
        }
    }

    private fun requiredFieldsMap(content: ContentDto): Map<String, Any?> {
        return mapOf(
            ID to content.id,
            HREF to content.href,
            TITLE to content.title,
            INGRESS to content.ingress,
            TEXT to content.text,
            METADATA to content.metadata,
            METADATA_CREATED_AT to content.metadata?.createdAt,
            METADATA_LAST_UPDATED to content.metadata?.lastUpdated,
            METADATA_AUDIENCE to content.metadata?.audience,
            METADATA_LANGUAGE to content.metadata?.language,
        )
    }

    private fun validateNotNull(fields: Map<String, Any?>): List<String> {
        return fields.entries.filter { it.value == null }.map { "Påkrevd felt mangler: ${it.key}" }
    }

    private fun validateType(type: String): List<String> {
        return listOfNotNull(validateValueIsValid<ValidTypes>(type, METADATA_TYPE))
    }

    private fun validateAudience(audience: List<String>): List<String> {
        return audience.mapNotNull { validateValueIsValid<ValidAudiences>(it, METADATA_AUDIENCE) }
    }

    private fun validateLanguage(value: String, fieldName: String): List<String> {
        val validLanguages = kodeverkConsumer.fetchSpraakKoder().koder
        return if (!validLanguages.contains(value.uppercase())) {
            listOf("Ugyldig verdi for $fieldName: $value. Må være tobokstavs språkkode fra kodeverk-api.")
        } else emptyList()
    }

    private fun validateFylke(fylke: String?): List<String> {
        return if (fylke != null) {
            listOfNotNull(validateValueIsValid<ValidFylker>(fylke, METADATA_FYLKE))
        } else emptyList()
    }

    private fun validateMetatags(metatags: List<String>): List<String> {
        return metatags.mapNotNull { validateValueIsValid<ValidMetatags>(it, METADATA_METATAGS) }
    }

    private fun validateLanguageRefs(languageRefs: List<String>): List<String> {
        return languageRefs.flatMap { validateLanguage(it, METADATA_LANGUAGE_REFS) }
    }

    private inline fun <reified T> validateValueIsValid(
        value: String,
        fieldName: String
    ): String? where T : Enum<T>, T : DescriptorProvider {
        return if (!enumContains<T>(value)) {
            "Ugyldig verdi for $fieldName: $value. Gyldige verdier: ${enumDescriptors<T>()}"
        } else null
    }
}