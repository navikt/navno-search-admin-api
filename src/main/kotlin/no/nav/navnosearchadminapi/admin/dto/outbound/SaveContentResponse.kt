package no.nav.navnosearchadminapi.admin.dto.outbound

data class SaveContentResponse(
    val numberOfIndexedDocuments: Int,
    val numberOfFailedDocuments: Int,
    val validationErrors: Map<String, List<String>>
)