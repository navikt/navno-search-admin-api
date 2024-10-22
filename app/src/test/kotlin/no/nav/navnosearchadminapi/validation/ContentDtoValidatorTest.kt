package no.nav.navnosearchadminapi.validation

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
import no.nav.navnosearchadminapi.common.enums.ValidAudiences
import no.nav.navnosearchadminapi.common.enums.ValidFylker
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.common.enums.ValidTypes
import no.nav.navnosearchadminapi.consumer.kodeverk.KodeverkConsumer
import no.nav.navnosearchadminapi.service.validation.ContentDtoValidator
import no.nav.navnosearchadminapi.utils.dummyContentDto
import no.nav.navnosearchadminapi.utils.enumDescriptors
import no.nav.navnosearchadminapi.utils.mockedKodeverkResponse
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
    fun `skal ha tom liste av valideringsfeil ved gyldig input`() {
        val content = listOf(dummyContentDto())
        val validationErrors = validator.validate(content)

        validationErrors.shouldBeEmpty()
    }

    @Test
    fun `skal ha tom liste av valideringsfeil ved gyldig input uten audience`() {
        val content = listOf(dummyContentDto(audience = emptyList()))
        val validationErrors = validator.validate(content)

        validationErrors.shouldBeEmpty()
    }

    @Test
    fun `skal returnere valideringsfeil for manglende påkrevd felt`() {
        val content = listOf(dummyContentDto(text = null))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Påkrevd felt mangler: text")
    }

    @Test
    fun `skal returnere valideringsfeil for ugyldig audience`() {
        val content = listOf(dummyContentDto(audience = listOf(invalidValue)))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Ugyldig verdi for metadata.audience: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidAudiences>()}")
    }

    @Test
    fun `skal returnere valideringsfeil for ugyldig type`() {
        val content = listOf(dummyContentDto(type = invalidValue))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Ugyldig verdi for metadata.type: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidTypes>()}")
    }

    @Test
    fun `skal returnere valideringsfeil for ugyldig fylke`() {
        val content = listOf(dummyContentDto(fylke = invalidValue))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Ugyldig verdi for metadata.fylke: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidFylker>()}")
    }

    @Test
    fun `skal returnere valideringsfeil for ugyldig metatag`() {
        val content = listOf(dummyContentDto(metatags = listOf(invalidValue)))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Ugyldig verdi for metadata.metatags: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidMetatags>()}")
    }

    @Test
    fun `skal returnere valideringsfeil for ugyldig språk`() {
        val content = listOf(dummyContentDto(language = invalidValue))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Ugyldig verdi for metadata.language: invalidValue. Må være tobokstavs språkkode fra kodeverk-api.")
    }

    @Test
    fun `skal returnere valideringsfeil for ugyldig språk i languageRefs`() {
        val content = listOf(dummyContentDto(languageRefs = listOf(invalidValue)))
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 1
        validationErrors[id]!!.shouldContainOnly("Ugyldig verdi for metadata.languageRefs: invalidValue. Må være tobokstavs språkkode fra kodeverk-api.")
    }

    @Test
    fun `skal returnere flere valideringsfeil ved flere ugyldige verdier i input`() {
        val firstId = "first"
        val secondId = "second"

        val content = listOf(
            dummyContentDto(id = firstId, language = invalidValue),
            dummyContentDto(id = secondId, audience = listOf(invalidValue), fylke = invalidValue)
        )
        val validationErrors = validator.validate(content)

        validationErrors shouldHaveSize 2

        validationErrors.shouldHaveKey(firstId)
        validationErrors[firstId]!!.shouldContainOnly("Ugyldig verdi for metadata.language: invalidValue. Må være tobokstavs språkkode fra kodeverk-api.")

        validationErrors.shouldHaveKey(secondId)
        validationErrors[secondId]!! shouldContainExactly (listOf(
            "Ugyldig verdi for metadata.audience: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidAudiences>()}",
            "Ugyldig verdi for metadata.fylke: $invalidValue. Gyldige verdier: ${enumDescriptors<ValidFylker>()}"
        ))
    }
}