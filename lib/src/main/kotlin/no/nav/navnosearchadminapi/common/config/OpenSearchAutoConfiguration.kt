package no.nav.navnosearchadminapi.common.config

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

/**
 * Auto-configuration for OpenSearch integration.
 * 
 * This configuration is automatically loaded by Spring Boot's auto-configuration mechanism.
 * It enables the OpenSearch properties and imports the necessary configuration classes.
 */
@AutoConfiguration
@EnableConfigurationProperties(OpenSearchProperties::class)
@Import(ElasticsearchConfig::class, ReadOnlyElasticsearchConfig::class)
class OpenSearchAutoConfiguration