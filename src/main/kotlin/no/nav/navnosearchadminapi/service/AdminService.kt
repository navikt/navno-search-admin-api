package no.nav.navnosearchadminapi.service

import no.nav.navnosearchadminapi.dto.inbound.ContentDto
import no.nav.navnosearchadminapi.dto.outbound.SaveContentResponse
import no.nav.navnosearchadminapi.exception.DocumentForTeamNameNotFoundException
import no.nav.navnosearchadminapi.repository.ContentRepository
import no.nav.navnosearchadminapi.service.mapper.ContentDtoMapper
import no.nav.navnosearchadminapi.service.mapper.ContentMapper
import no.nav.navnosearchadminapi.service.validation.ContentDtoValidator
import no.nav.navnosearchadminapi.utils.createInternalId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AdminService(
    val validator: ContentDtoValidator,
    val contentDtoMapper: ContentDtoMapper,
    val contentMapper: ContentMapper,
    val repository: ContentRepository,
    @Value("\${opensearch.page-size}") val pageSize: Int,
) {
    val logger: Logger = LoggerFactory.getLogger(AdminService::class.java)

    fun saveAllContent(content: List<ContentDto>, teamName: String): SaveContentResponse {
        val validationErrors = validator.validate(content)

        if (validationErrors.isNotEmpty()) {
            logger.info("Fikk valideringsfeil ved indeksering av innhold: $validationErrors")
        }

        val mappedContent = content
            .filter { !validationErrors.containsKey(it.id) }
            .map { contentMapper.toContentDao(it, teamName) }
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
            throw DocumentForTeamNameNotFoundException("Dokument med ekstern id $externalId finnes ikke for team $teamName")
        }
    }

    fun getContentForTeamName(teamName: String, page: Int): Page<ContentDto> {
        val pageable = PageRequest.of(page, pageSize)
        return repository.findAllByTeamOwnedBy(teamName, pageable).map { contentDtoMapper.toContentDto(it) }
    }
}