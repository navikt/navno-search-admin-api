package no.nav.navnosearchadminapi.service.validation

import no.nav.navnosearchadminapi.common.constants.HREF
import no.nav.navnosearchadminapi.common.constants.ID
import no.nav.navnosearchadminapi.common.constants.INGRESS
import no.nav.navnosearchadminapi.common.constants.METADATA
import no.nav.navnosearchadminapi.common.constants.METADATA_AUDIENCE
import no.nav.navnosearchadminapi.common.constants.METADATA_CREATED_AT
import no.nav.navnosearchadminapi.common.constants.METADATA_FYLKE
import no.nav.navnosearchadminapi.common.constants.METADATA_LANGUAGE
import no.nav.navnosearchadminapi.common.constants.METADATA_LAST_UPDATED
import no.nav.navnosearchadminapi.common.constants.METADATA_METATAGS
import no.nav.navnosearchadminapi.common.constants.TEXT
import no.nav.navnosearchadminapi.common.constants.TITLE
import no.nav.navnosearchadminapi.common.enums.DescriptorProvider
import no.nav.navnosearchadminapi.common.enums.ValidAudiences
import no.nav.navnosearchadminapi.common.enums.ValidFylker
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.consumer.kodeverk.KodeverkConsumer
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.exception.MissingIdException
import no.nav.navnosearchadminapi.utils.enumContains
import no.nav.navnosearchadminapi.utils.enumDescriptors
import org.springframework.stereotype.Component

@Component
class ContentDtoValidator(val kodeverkConsumer: KodeverkConsumer) {

    fun validate(content: List<ContentDto>): MutableMap<String, MutableList<String>> {
        val validationErrors = mutableMapOf<String, MutableList<String>>()

        content.forEach {
            val errorMessages = mutableListOf<String>()

            errorMessages.addAll(validateNotNull(requiredFieldsMap(it)))

            it.metadata?.audience?.let { audience -> errorMessages.addAll(validateAudience(audience)) }
            it.metadata?.language?.let { language -> errorMessages.addAll(validateLanguage(language)) }
            it.metadata?.fylke?.let { fylke -> errorMessages.addAll(validateFylke(fylke)) }
            it.metadata?.metatags?.let { metatags -> errorMessages.addAll(validateMetatags(metatags)) }

            errorMessages.forEach { msg ->
                validationErrors.putIfAbsent(it.id ?: throw MissingIdException(), mutableListOf())
                validationErrors[it.id]!!.add(msg)
            }
        }

        return validationErrors
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
        return fields.mapNotNull { entry -> if (entry.value == null) "Påkrevd felt mangler: ${entry.key}" else null }
    }

    private fun validateAudience(audience: List<String>): List<String> {
        return if (audience.isEmpty()) {
            listOf("$METADATA_AUDIENCE må inneholde minst ett element")
        } else audience.mapNotNull { validateValueIsValid<ValidAudiences>(it, METADATA_AUDIENCE) }
    }

    private fun validateLanguage(language: String): List<String> {
        val validLanguages = kodeverkConsumer.fetchSpraakKoder().koder
        return if (!validLanguages.contains(language.uppercase())) {
            listOf("Ugyldig språkkode: $language. Må være tobokstavs språkkode fra kodeverk-api.")
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

    private inline fun <reified T> validateValueIsValid(
        value: String,
        fieldName: String
    ): String? where T : Enum<T>, T : DescriptorProvider {
        return if (!enumContains<T>(value)) {
            "Ugyldig verdi for $fieldName: $value. Gyldige verdier: ${enumDescriptors<T>()}"
        } else null
    }
}