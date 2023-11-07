package no.nav.navnosearchadminapi.service.mapper

import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.utils.dummyContentDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ContentDtoMapperTest {

    val externalId = "123"
    val mapper = ContentDtoMapper()

    @Test
    fun testMapping() {
        val contentDao = dummyContentDao(externalId = externalId, textPrefix = "test")
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.id).isEqualTo(externalId)
        assertThat(mappedContent.href).isEqualTo(contentDao.href)
        assertThat(mappedContent.title).isEqualTo(contentDao.title.no)
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.no)
        assertThat(mappedContent.text).isEqualTo(contentDao.text.no)
        assertThat(mappedContent.metadata).isNotNull()
        assertThat(mappedContent.metadata!!.createdAt).isEqualTo(contentDao.createdAt)
        assertThat(mappedContent.metadata!!.lastUpdated).isEqualTo(contentDao.lastUpdated)
        assertThat(mappedContent.metadata!!.audience).isEqualTo(contentDao.audience)
        assertThat(mappedContent.metadata!!.language).isEqualTo(contentDao.language)
        assertThat(mappedContent.metadata!!.isFile).isEqualTo(contentDao.isFile)
        assertThat(mappedContent.metadata!!.fylke).isEqualTo(contentDao.fylke)
        assertThat(mappedContent.metadata!!.metatags).isEqualTo(contentDao.metatags)
        assertThat(mappedContent.metadata!!.keywords).isEqualTo(contentDao.keywords)
    }

    @Test
    fun testMappingWithNynorskLanguage() {
        val contentDao = dummyContentDao(externalId = externalId, textPrefix = "test", language = NORWEGIAN_NYNORSK)
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.title).isEqualTo(contentDao.title.no)
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.no)
        assertThat(mappedContent.text).isEqualTo(contentDao.text.no)
    }

    @Test
    fun testMappingWithEnglishLanguage() {
        val contentDao = dummyContentDao(externalId = externalId, textPrefix = "test", language = ENGLISH)
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.title).isEqualTo(contentDao.title.en)
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.en)
        assertThat(mappedContent.text).isEqualTo(contentDao.text.en)
    }

    @Test
    fun testMappingWithUnsupportedLanguage() {
        val contentDao = dummyContentDao(externalId = externalId, textPrefix = "test", language = "se")
        val mappedContent = mapper.toContentDto(contentDao)

        assertThat(mappedContent.title).isEqualTo(contentDao.title.other)
        assertThat(mappedContent.ingress).isEqualTo(contentDao.ingress.other)
        assertThat(mappedContent.text).isEqualTo(contentDao.text.other)
    }
}