package no.nav.navnosearchadminapi.dto.inbound

import no.nav.navnosearchadminapi.common.enums.ValidTypes
import java.time.ZonedDateTime

data class ContentMetadata(
    val type: String = ValidTypes.DEFAULT.descriptor,
    val createdAt: ZonedDateTime? = null,
    val lastUpdated: ZonedDateTime? = null,
    val audience: List<String>? = null,
    val language: String? = null,
    val isFile: Boolean = false,
    val fylke: String? = null,
    val metatags: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),
)