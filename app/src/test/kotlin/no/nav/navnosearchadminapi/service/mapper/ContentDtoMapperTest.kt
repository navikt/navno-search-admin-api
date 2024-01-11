package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.utils.dummyContentDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ContentDtoMapperTest {

    val mapper = ContentDtoMapper()

    val externalId = "123"

    @Test
    fun testMapping() {
        val contentDao = dummyContentDao(externalId = externalId)
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.id).isEqualTo(externalId)
        assertThat(mappedContent.href).isEqualTo(contentDao.href)
        assertThat(mappedContent.title).isEqualTo(contentDao.title.no.first())
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.no.first())
        assertThat(mappedContent.text).isEqualTo(contentDao.text.no.first())
        assertThat(mappedContent.metadata).isNotNull()
        assertThat(mappedContent.metadata!!.type).isEqualTo(contentDao.type)
        assertThat(mappedContent.metadata!!.createdAt).isEqualTo(contentDao.createdAt)
        assertThat(mappedContent.metadata!!.lastUpdated).isEqualTo(contentDao.lastUpdated)
        assertThat(mappedContent.metadata!!.audience).isEqualTo(contentDao.audience)
        assertThat(mappedContent.metadata!!.language).isEqualTo(contentDao.language)
        assertThat(mappedContent.metadata!!.fylke).isEqualTo(contentDao.fylke)
        assertThat(mappedContent.metadata!!.metatags).isEqualTo(contentDao.metatags)
        assertThat(mappedContent.metadata!!.keywords).isEqualTo(contentDao.keywords)
    }

    @Test
    fun testMappingWithNynorskLanguage() {
        val contentDao = dummyContentDao(externalId = externalId, language = NORWEGIAN_NYNORSK)
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.title).isEqualTo(contentDao.title.no.first())
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.no.first())
        assertThat(mappedContent.text).isEqualTo(contentDao.text.no.first())
    }

    @Test
    fun testMappingWithEnglishLanguage() {
        val contentDao = dummyContentDao(externalId = externalId, language = ENGLISH)
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.title).isEqualTo(contentDao.title.en.first())
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.en.first())
        assertThat(mappedContent.text).isEqualTo(contentDao.text.en.first())
    }

    @Test
    fun testMappingWithUnsupportedLanguage() {
        val contentDao = dummyContentDao(externalId = externalId, language = "se")
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.title).isEqualTo(contentDao.title.other.first())
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.other.first())
        assertThat(mappedContent.text).isEqualTo(contentDao.text.other.first())
    }
}