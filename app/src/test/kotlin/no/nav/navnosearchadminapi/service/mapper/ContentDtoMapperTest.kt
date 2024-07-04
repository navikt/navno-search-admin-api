package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.utils.dummyContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ContentDtoMapperTest {

    val mapper = ContentDtoMapper()

    val externalId = "123"

    @Test
    fun testMapping() {
        val content = dummyContent(externalId = externalId)
        val mappedContent = mapper.toContentDto(content)

        assertThat(mappedContent.id).isEqualTo(externalId)
        assertThat(mappedContent.href).isEqualTo(content.href)
        assertThat(mappedContent.title).isEqualTo(content.title.no)
        assertThat(mappedContent.ingress).isEqualTo(content.ingress.no)
        assertThat(mappedContent.text).isEqualTo(content.text.no)
        assertThat(mappedContent.metadata).isNotNull()
        assertThat(mappedContent.metadata!!.type).isEqualTo(content.type)
        assertThat(mappedContent.metadata!!.createdAt).isEqualTo(content.createdAt)
        assertThat(mappedContent.metadata!!.lastUpdated).isEqualTo(content.lastUpdated)
        assertThat(mappedContent.metadata!!.audience).isEqualTo(content.audience)
        assertThat(mappedContent.metadata!!.language).isEqualTo(content.language)
        assertThat(mappedContent.metadata!!.fylke).isEqualTo(content.fylke)
        assertThat(mappedContent.metadata!!.metatags).isEqualTo(content.metatags)
        assertThat(mappedContent.metadata!!.keywords).isEqualTo(content.keywords)
    }

    @Test
    fun testMappingWithNynorskLanguage() {
        val content = dummyContent(externalId = externalId, language = NORWEGIAN_NYNORSK)
        val mappedContent = mapper.toContentDto(content)

        assertThat(mappedContent.title).isEqualTo(content.title.no)
        assertThat(mappedContent.ingress).isEqualTo(content.ingress.no)
        assertThat(mappedContent.text).isEqualTo(content.text.no)
    }

    @Test
    fun testMappingWithEnglishLanguage() {
        val content = dummyContent(externalId = externalId, language = ENGLISH)
        val mappedContent = mapper.toContentDto(content)

        assertThat(mappedContent.title).isEqualTo(content.title.en)
        assertThat(mappedContent.ingress).isEqualTo(content.ingress.en)
        assertThat(mappedContent.text).isEqualTo(content.text.en)
    }

    @Test
    fun testMappingWithUnsupportedLanguage() {
        val content = dummyContent(externalId = externalId, language = "se")
        val mappedContent = mapper.toContentDto(content)

        assertThat(mappedContent.title).isEqualTo(content.title.other)
        assertThat(mappedContent.ingress).isEqualTo(content.ingress.other)
        assertThat(mappedContent.text).isEqualTo(content.text.other)
    }
}