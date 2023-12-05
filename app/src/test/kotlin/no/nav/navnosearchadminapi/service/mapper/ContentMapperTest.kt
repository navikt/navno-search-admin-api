package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.NORWEGIAN
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.utils.STATISTIKK
import no.nav.navnosearchadminapi.utils.dummyContentDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ContentMapperTest {

    val mapper = ContentMapper()

    val teamName = "testteam"

    @Test
    fun testMapping() {
        val contentDto = dummyContentDto(metatags = listOf(STATISTIKK))
        val mappedContent = mapper.toContentDao(contentDto, teamName)

        assertThat(mappedContent.id).isEqualTo("$teamName-${contentDto.id}")
        assertThat(mappedContent.autocomplete.input.contains(contentDto.title)).isTrue()
        assertThat(mappedContent.teamOwnedBy).isEqualTo(teamName)
        assertThat(mappedContent.href).isEqualTo(contentDto.href)
        assertThat(mappedContent.title.en).isEqualTo(contentDto.title)
        assertThat(mappedContent.ingress.en).isEqualTo(contentDto.ingress)
        assertThat(mappedContent.text.en).isEqualTo(contentDto.text)
        assertThat(mappedContent.type).isEqualTo(contentDto.metadata!!.type)
        assertThat(mappedContent.createdAt).isEqualTo(contentDto.metadata!!.createdAt)
        assertThat(mappedContent.lastUpdated).isEqualTo(contentDto.metadata!!.lastUpdated)
        assertThat(mappedContent.audience).isEqualTo(contentDto.metadata!!.audience)
        assertThat(mappedContent.language).isEqualTo(contentDto.metadata!!.language)
        assertThat(mappedContent.isFile).isEqualTo(contentDto.metadata!!.isFile)
        assertThat(mappedContent.fylke).isEqualTo(contentDto.metadata!!.fylke)
        assertThat(mappedContent.metatags).isEqualTo(contentDto.metadata!!.metatags)
        assertThat(mappedContent.keywords).isEqualTo(contentDto.metadata!!.keywords)
    }

    @Test
    fun testMappingWithHtmlInText() {
        val textWithHtml = "<p>Text with</p> html <br/>"
        val expectedFilteredText = "Text with html"

        val contentDto = dummyContentDto(text = textWithHtml)
        val mappedContent = mapper.toContentDao(contentDto, teamName)

        assertThat(mappedContent.text.en).isEqualTo(expectedFilteredText)
    }

    @Test
    fun testMappingWithMacrosInText() {
        val textWithMacros = "Text with macro[macro/]"
        val expectedFilteredText = "Text with macro"

        val contentDto = dummyContentDto(text = textWithMacros)
        val mappedContent = mapper.toContentDao(contentDto, teamName)

        assertThat(mappedContent.text.en).isEqualTo(expectedFilteredText)
    }

    @Test
    fun testMappingWithoutMetatags() {
        val contentDto = dummyContentDto()
        val mappedContent = mapper.toContentDao(contentDto, teamName)

        assertThat(mappedContent.metatags).isEqualTo(listOf(ValidMetatags.INFORMASJON.descriptor))
    }

    @Test
    fun testMappingWithNorwegianNynorskLanguage() {
        val contentDto = dummyContentDto(language = NORWEGIAN_NYNORSK)
        val mappedContent = mapper.toContentDao(contentDto, teamName)

        assertThat(mappedContent.language).isEqualTo(contentDto.metadata!!.language)

        assertThat(mappedContent.title.no).isEqualTo(contentDto.title)
        assertThat(mappedContent.ingress.no).isEqualTo(contentDto.ingress)
        assertThat(mappedContent.text.no).isEqualTo(contentDto.text)
        assertThat(mappedContent.title.en).isNull()
        assertThat(mappedContent.ingress.en).isNull()
        assertThat(mappedContent.text.en).isNull()
        assertThat(mappedContent.title.other).isNull()
        assertThat(mappedContent.ingress.other).isNull()
        assertThat(mappedContent.text.other).isNull()
    }

    @Test
    fun testMappingWithGenericNorwegianLanguageShouldMapToBokmaal() {
        val contentDto = dummyContentDto(language = NORWEGIAN)
        val mappedContent = mapper.toContentDao(contentDto, teamName)

        assertThat(mappedContent.language).isEqualTo(NORWEGIAN_BOKMAAL)

        assertThat(mappedContent.title.no).isEqualTo(contentDto.title)
        assertThat(mappedContent.ingress.no).isEqualTo(contentDto.ingress)
        assertThat(mappedContent.text.no).isEqualTo(contentDto.text)
    }
}