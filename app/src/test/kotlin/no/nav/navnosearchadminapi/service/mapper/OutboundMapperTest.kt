package no.nav.navnosearchadminapi.service.mapper

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.nav.navnosearchadminapi.common.constants.ENGLISH
import no.nav.navnosearchadminapi.common.constants.NORWEGIAN_NYNORSK
import no.nav.navnosearchadminapi.utils.TEAM_NAME
import no.nav.navnosearchadminapi.utils.dummyContent
import org.junit.jupiter.api.Test

class OutboundMapperTest {

    @Test
    fun testMapping() {
        val content = dummyContent()
        val mappedContent = content.toOutbound()

        assertSoftly(mappedContent) {
            id shouldBe content.id.substringAfter("$TEAM_NAME-")
            href shouldBe content.href
            title shouldBe content.title.no
            ingress shouldBe content.ingress.no
            text shouldBe content.text.no
            metadata.shouldNotBeNull()

            with(metadata!!) {
                type shouldBe content.type
                createdAt shouldBe content.createdAt
                lastUpdated shouldBe content.lastUpdated
                audience shouldBe content.audience
                language shouldBe content.language
                fylke shouldBe content.fylke
                metatags shouldBe content.metatags
                languageRefs.shouldBeEmpty()  //todo: bør være noe her
                keywords shouldBe content.keywords
            }
        }
    }

    @Test
    fun testMappingWithNynorskLanguage() {
        val content = dummyContent(language = NORWEGIAN_NYNORSK)
        val mappedContent = content.toOutbound()

        assertSoftly(mappedContent) {
            title shouldBe content.title.no
            ingress shouldBe content.ingress.no
            text shouldBe content.text.no
        }
    }

    @Test
    fun testMappingWithEnglishLanguage() {
        val content = dummyContent(language = ENGLISH)
        val mappedContent = content.toOutbound()

        assertSoftly(mappedContent) {
            title shouldBe content.title.en
            ingress shouldBe content.ingress.en
            text shouldBe content.text.en
        }
    }

    @Test
    fun testMappingWithUnsupportedLanguage() {
        val content = dummyContent(language = "se")
        val mappedContent = content.toOutbound()

        mappedContent.title shouldBe content.title.other
        mappedContent.ingress shouldBe content.ingress.other
        mappedContent.text shouldBe content.text.other
    }
}