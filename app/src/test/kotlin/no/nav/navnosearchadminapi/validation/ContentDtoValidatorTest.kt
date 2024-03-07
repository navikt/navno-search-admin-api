package no.nav.navnosearchadminapi.validation

import no.nav.navnosearchadminapi.common.enums.ValidAudiences
import no.nav.navnosearchadminapi.common.enums.ValidFylker
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.consumer.kodeverk.KodeverkConsumer
import no.nav.navnosearchadminapi.service.validation.ContentDtoValidator
import no.nav.navnosearchadminapi.utils.dummyContentDto
import no.nav.navnosearchadminapi.utils.enumDescriptors
import no.nav.navnosearchadminapi.utils.mockedKodeverkResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ContentDtoValidatorTest(@Mock val kodeverkConsumer: KodeverkConsumer) {

    private val invalidValue = "invalidValue"
    private val id = dummyContentDto().id

    private val validator = ContentDtoValidator(kodeverkConsumer)

    @BeforeEach
    fun setup() {
        Mockito.`when`(kodeverkConsumer.fetchSpraakKoder()).thenReturn(mockedKodeverkResponse)
    }

    @Test
    fun testValidation() {
        val content = listOf(dummyContentDto())
        val validationErrors = validator.validate(content)
        assertThat(validationErrors).isEmpty()
    }

    @Test
    fun testValidationWithMissingAudience() {
        val content = listOf(dummyContentDto(audience = emptyList()))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).isEmpty()
    }

    @Test
    fun testValidationWithMissingRequiredField() {
        val content = listOf(dummyContentDto(text = null))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Påkrevd felt mangler: text")
    }

    @Test
    fun testValidationWithInvalidAudience() {
        val content = listOf(dummyContentDto(audience = listOf(invalidValue)))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Ugyldig verdi for metadata.audience: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidAudiences>()}")
    }

    @Test
    fun testValidationWithInvalidType() {
        val content = listOf(dummyContentDto(type = invalidValue))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Ugyldig verdi for metadata.type: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidTypes>()}")
    }

    @Test
    fun testValidationWithInvalidFylke() {
        val content = listOf(dummyContentDto(fylke = invalidValue))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Ugyldig verdi for metadata.fylke: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidFylker>()}")
    }

    @Test
    fun testValidationWithInvalidMetatag() {
        val content = listOf(dummyContentDto(metatags = listOf(invalidValue)))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Ugyldig verdi for metadata.metatags: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidMetatags>()}")
    }

    @Test
    fun testValidationWithInvalidLanguage() {
        val content = listOf(dummyContentDto(language = invalidValue))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Ugyldig verdi for metadata.language: invalidValue. Må være tobokstavs språkkode fra kodeverk-api.")
    }

    @Test
    fun testValidationWithInvalidLanguageRef() {
        val content = listOf(dummyContentDto(languageRefs = listOf(invalidValue)))
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(1)
        assertThat(validationErrors[id]).hasSize(1)
        assertThat(validationErrors[id]!!.first()).isEqualTo("Ugyldig verdi for metadata.languageRefs: invalidValue. Må være tobokstavs språkkode fra kodeverk-api.")
    }

    @Test
    fun testValidationWithMultipleValidationErrors() {
        val firstId = "first"
        val secondId = "second"

        val content = listOf(
            dummyContentDto(id = firstId, language = invalidValue),
            dummyContentDto(id = secondId, audience = listOf(invalidValue), fylke = invalidValue)
        )
        val validationErrors = validator.validate(content)

        assertThat(validationErrors).hasSize(2)
        assertThat(validationErrors[firstId]).hasSize(1)
        assertThat(validationErrors[secondId]).hasSize(2)
        assertThat(validationErrors[firstId]!!.first()).isEqualTo("Ugyldig verdi for metadata.language: invalidValue. Må være tobokstavs språkkode fra kodeverk-api.")
        assertThat(validationErrors[secondId]!!).contains("Ugyldig verdi for metadata.audience: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidAudiences>()}")
        assertThat(validationErrors[secondId]!!).contains("Ugyldig verdi for metadata.fylke: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidFylker>()}")
    }
}