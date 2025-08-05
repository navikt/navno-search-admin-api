package no.nav.navnosearchadminapi.common.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.IndexOperations
import org.springframework.data.elasticsearch.core.RefreshPolicy
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository
import no.nav.navnosearchadminapi.common.model.Content
import no.nav.navnosearchadminapi.common.repository.ContentRepository
import no.nav.navnosearchadminapi.common.constants.IndexInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport

/**
 * Configuration for read-only Elasticsearch/OpenSearch access.
 * 
 * This configuration is activated when 'opensearch.access-mode' is set to 'read'.
 * It provides a custom repository implementation that prevents write operations
 * and index management operations.
 */
@Configuration
@ConditionalOnProperty(
    name = ["opensearch.access-mode"],
    havingValue = "read"
)
class ReadOnlyElasticsearchConfig {

    @Bean
    fun contentRepository(operations: ElasticsearchOperations): ContentRepository {
        return ReadOnlyContentRepository(operations)
    }
    
    /**
     * Read-only implementation of ContentRepository.
     * This implementation allows only read operations and prevents any write
     * or index management operations that would require admin permissions.
     */
    class ReadOnlyContentRepository(
        private val operations: ElasticsearchOperations
    ) : ContentRepository {
        
        private val indexCoordinates = IndexCoordinates.of(IndexInfo.INDEX_NAME)
        
        override fun findById(id: String): java.util.Optional<Content> {
            val content = operations.get(id, Content::class.java, indexCoordinates)
            return java.util.Optional.ofNullable(content)
        }
        
        override fun existsById(id: String): Boolean {
            return operations.exists(id, indexCoordinates)
        }
        
        override fun findAll(): Iterable<Content> {
            throw UnsupportedOperationException("Read-only mode: findAll() is not supported")
        }
        
        override fun findAllById(ids: Iterable<String>): Iterable<Content> {
            return operations.multiGet(
                operations.idsQuery(ids.toList()),
                Content::class.java,
                indexCoordinates
            )
        }
        
        override fun count(): Long {
            return operations.count(operations.matchAllQuery(), Content::class.java, indexCoordinates)
        }
        
        override fun deleteById(id: String) {
            throw UnsupportedOperationException("Read-only mode: deleteById() is not supported")
        }
        
        override fun delete(entity: Content) {
            throw UnsupportedOperationException("Read-only mode: delete() is not supported")
        }
        
        override fun deleteAllById(ids: Iterable<String>) {
            throw UnsupportedOperationException("Read-only mode: deleteAllById() is not supported")
        }
        
        override fun deleteAll(entities: Iterable<Content>) {
            throw UnsupportedOperationException("Read-only mode: deleteAll() is not supported")
        }
        
        override fun deleteAll() {
            throw UnsupportedOperationException("Read-only mode: deleteAll() is not supported")
        }
        
        override fun <S : Content> save(entity: S): S {
            throw UnsupportedOperationException("Read-only mode: save() is not supported")
        }
        
        override fun <S : Content> saveAll(entities: Iterable<S>): Iterable<S> {
            throw UnsupportedOperationException("Read-only mode: saveAll() is not supported")
        }
        
        override fun findAllByTeamOwnedBy(teamOwnedBy: String, pageable: Pageable): Page<Content> {
            val query = operations.criteriaQuery(
                operations.criteria("teamOwnedBy").`is`(teamOwnedBy)
            ).setPageable(pageable)
            
            return operations.searchForPage(query, Content::class.java, indexCoordinates)
        }
    }
}