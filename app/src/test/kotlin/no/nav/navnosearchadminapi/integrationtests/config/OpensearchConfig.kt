package no.nav.navnosearchadminapi.integrationtests.config

import org.opensearch.testcontainers.OpenSearchContainer
import org.springframework.boot.test.context.TestConfiguration
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@TestConfiguration
class OpensearchConfig {
    companion object {
        private val opensearch: OpenSearchContainer<*> =
            OpenSearchContainer(
                DockerImageName.parse("opensearchproject/opensearch:2.11.1")
            )
                .withStartupAttempts(5)
                .withStartupTimeout(Duration.ofMinutes(2))

        init {
            opensearch.start()
            System.setProperty("opensearch.uris", opensearch.httpHostAddress)
        }
    }
}