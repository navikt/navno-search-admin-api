package no.nav.navnosearchadminapi.service

import no.nav.navnosearchadminapi.repository.ContentRepository
import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.outbound.SaveContentResponse
import no.nav.navnosearchadminapi.service.mapper.toInbound
import no.nav.navnosearchadminapi.service.mapper.toOutbound
import no.nav.navnosearchadminapi.service.validation.ContentDtoValidator
import no.nav.navnosearchadminapi.utils.createInternalId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper

@Service
class AdminService(
    val validator: ContentDtoValidator,
    val repository: ContentRepository,
    @param:Value("\${opensearch.page-size}") val pageSize: Int,
    val objectMapper: ObjectMapper,
) {
    val logger: Logger = LoggerFactory.getLogger(AdminService::class.java)

    fun saveAllContent(content: List<ContentDto>, teamName: String, xpOrigin: String?): SaveContentResponse {
        if (xpOrigin in SMOKE_TEST_XP_ORIGINS) {
            content.forEach {
                logger.info(
                    "Search document received as smoke test from $xpOrigin. " +
                        "Dropping indexing. Document: ${objectMapper.writeValueAsString(it)}"
                )
            }
            return SaveContentResponse(numberOfIndexedDocuments = 0, numberOfFailedDocuments = 0, validationErrors = emptyMap())
        }

        val validationErrors = validator.validate(content)

        if (validationErrors.isNotEmpty()) {
            logger.warn("Fikk valideringsfeil ved indeksering av innhold: $validationErrors")
        }

        val mappedContent = content
            .filter { !validationErrors.containsKey(it.id) }
            .map { it.toInbound(teamName) }
        repository.saveAll(mappedContent)

        val numberOfIndexedDocuments = mappedContent.size
        val numberOfFailedDocuments = validationErrors.size
        logger.info("$numberOfIndexedDocuments dokumenter indeksert, $numberOfFailedDocuments dokumenter feilet")

        return SaveContentResponse(numberOfIndexedDocuments, numberOfFailedDocuments, validationErrors)
    }

    fun deleteContentByTeamNameAndId(teamName: String, externalId: String) {
        val id = createInternalId(teamName, externalId)
        if (repository.existsById(id)) {
            repository.deleteById(id)
        } else {
            logger.info("Forsøkte å slette dokument med ekstern id $externalId, men dette finnes ikke for team $teamName")
        }
    }

    fun getContentForTeamName(teamName: String, page: Int): Page<ContentDto> {
        val pageable = PageRequest.of(page, pageSize)
        return repository.findAllByTeamOwnedBy(teamName, pageable).map { it.toOutbound() }
    }

    companion object {
        // xp-origin header values for XP dev environments that only run smoke tests and whose
        // documents must never be indexed.
        private val SMOKE_TEST_XP_ORIGINS = setOf("q6", "dev3")
    }
}