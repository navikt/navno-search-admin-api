package no.nav.navnosearchadminapi.common.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

/**
 * Configuration for Elasticsearch/OpenSearch repositories.
 * 
 * This configuration enables Elasticsearch repositories when the property
 * 'opensearch.repository.enabled' is set to true (default).
 * 
 * For applications with read-only access, set 'opensearch.repository.enabled=false'
 * in your application properties and manually configure the repositories as needed.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = ["no.nav.navnosearchadminapi.common.repository"])
@ConditionalOnProperty(
    name = ["opensearch.repository.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class ElasticsearchConfig