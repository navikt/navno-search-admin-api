package no.nav.navnosearchadminapi.common.constants

/**
 * Provides index information for the OpenSearch content index.
 * 
 * This class contains static information about the index that can be
 * accessed by both read-only and admin consumers without requiring
 * any OpenSearch operations.
 */
object IndexInfo {
    /**
     * The name of the content index.
     * Note: After moving to the 'navno' namespace in Nais, v1-v7 no longer exists as the
     * opensearch instance is new. However, we're keeping with the versioning for continuity.
     */
    const val INDEX_NAME = "search-content-v8"
    
    /**
     * The current version of the index.
     */
    const val INDEX_VERSION = 8
    
    /**
     * The index name prefix without version.
     */
    const val INDEX_PREFIX = "search-content"
    
    /**
     * Gets the full index name with version.
     */
    fun getIndexName(): String = INDEX_NAME
    
    /**
     * Gets the index version as an integer.
     */
    fun getIndexVersion(): Int = INDEX_VERSION
}