package no.nav.navnosearchadminapi.service.mapper

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_BOKMAAL
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.common.enums.ValidMetatags
import no.nav.navnosearchadminapi.utils.TEAM_NAME
import no.nav.navnosearchadminapi.utils.dummyContentDto
import org.junit.jupiter.api.Test

class InboundMapperTest {

    @Test
    fun `skal mappe alle felter riktig`() {
        val contentDto = dummyContentDto(metatags = listOf(ValidMetatags.STATISTIKK.descriptor))
        val mappedContent = contentDto.toInbound(TEAM_NAME)

        assertSoftly(mappedContent) {
            id shouldBe "$TEAM_NAME-${contentDto.id}"
            teamOwnedBy shouldBe TEAM_NAME
            href shouldBe contentDto.href
            title.en shouldBe contentDto.title
            ingress.en shouldBe contentDto.ingress
            text.en shouldBe contentDto.text
            type shouldBe contentDto.metadata!!.type
            createdAt shouldBe contentDto.metadata!!.createdAt
            lastUpdated shouldBe contentDto.metadata!!.lastUpdated
            audience shouldBe contentDto.metadata!!.audience
            language shouldBe contentDto.metadata!!.language
            fylke shouldBe contentDto.metadata!!.fylke
            metatags shouldBe contentDto.metadata!!.metatags
            languageRefs.shouldBeEmpty() //todo: bør være noe her
        }
    }

    @Test
    fun `skal filtrere ut html fra text`() {
        val textWithHtml = "<p>Text with</p> html <br/>"
        val expectedFilteredText = "Text with html"

        val contentDto = dummyContentDto(text = textWithHtml)
        val mappedContent = contentDto.toInbound(TEAM_NAME)

        mappedContent.text.en shouldBe expectedFilteredText
    }

    @Test
    fun `skal filtrere ut macros fra text`() {
        val textWithMacros = "Text with macro[macro/]"
        val expectedFilteredText = "Text with macro"

        val contentDto = dummyContentDto(text = textWithMacros)
        val mappedContent = contentDto.toInbound(TEAM_NAME)

        mappedContent.text.en shouldBe expectedFilteredText
    }

    @Test
    fun `skal defaulte manglende metatag til informasjon`() {
        val contentDto = dummyContentDto(metatags = emptyList())
        val mappedContent = contentDto.toInbound(TEAM_NAME)

        mappedContent.metatags shouldBe listOf(ValidMetatags.INFORMASJON.descriptor)
    }

    @Test
    fun `skal mappe nynorsk til no-felter`() {
        val contentDto = dummyContentDto(language = NORWEGIAN_NYNORSK)
        val mappedContent = contentDto.toInbound(TEAM_NAME)

        mappedContent.language shouldBe contentDto.metadata!!.language

        assertSoftly(mappedContent) {
            language shouldBe NORWEGIAN_NYNORSK
            title.no shouldBe contentDto.title
            ingress.no shouldBe contentDto.ingress
            text.no shouldBe contentDto.text
            title.en.shouldBeNull()
            ingress.en.shouldBeNull()
            text.en.shouldBeNull()
            title.other.shouldBeNull()
            ingress.other.shouldBeNull()
            text.other.shouldBeNull()
        }
    }

    @Test
    fun `skal mappe generisk norsk språkkode til bokmål`() {
        val contentDto = dummyContentDto(language = NORWEGIAN)
        val mappedContent = contentDto.toInbound(TEAM_NAME)

        assertSoftly(mappedContent) {
            language shouldBe NORWEGIAN_BOKMAAL
            title.no shouldBe contentDto.title
            ingress.no shouldBe contentDto.ingress
            text.no shouldBe contentDto.text
        }
    }
}