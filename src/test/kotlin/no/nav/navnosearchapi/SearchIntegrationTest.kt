package no.nav.navnosearchapi

import no.nav.navnosearchapi.dto.ContentSearchPage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.getForEntity

class SearchIntegrationTest : AbstractIntegrationTest() {

    @BeforeEach
    fun setup() {
        setupIndex()
    }

    @Test
    fun testSearchForText() {
        val term = "First text"

        val result = restTemplate.getForEntity<ContentSearchPage>(searchUrl(term)).body!!

        assertThat(result.totalElements).isEqualTo(10L)
        assertThat(result.totalPages).isEqualTo(1L)
    }

    @Test
    fun testSearchForTextWithMaalgruppeFilter() {
        val term = "First text"
        val maalgruppe = "Privatperson"

        val result = restTemplate.getForEntity<ContentSearchPage>(searchUrl(term, maalgruppe = maalgruppe)).body!!

        assertThat(result.totalElements).isEqualTo(4L)
        assertThat(result.totalPages).isEqualTo(1L)
    }

    @Test
    fun testSearchForPhrase() {
        val term = "\"First text\""

        val result = restTemplate.getForEntity<ContentSearchPage>(searchUrl(term)).body!!

        assertThat(result.totalElements).isEqualTo(1L)
        assertThat(result.totalPages).isEqualTo(1L)
    }
}