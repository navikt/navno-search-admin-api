package no.nav.navnosearchadminapi.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for OpenSearch.
 * 
 * These properties control the behavior of the OpenSearch integration,
 * allowing applications to operate in different modes based on their access level.
 */
@ConfigurationProperties(prefix = "opensearch")
data class OpenSearchProperties(
    /**
     * Whether to enable automatic repository initialization.
     * Set to false for read-only access to prevent index creation/updates.
     * Default: true
     */
    val repositoryEnabled: Boolean = true,
    
    /**
     * The access mode for OpenSearch operations.
     * Values: "admin" (full access) or "read" (read-only access)
     * Default: "admin"
     */
    val accessMode: String = "admin"
)