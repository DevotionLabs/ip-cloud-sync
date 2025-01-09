package labs.devotion.ipcloudsync.cloudflareclient.formats

import kotlinx.serialization.Serializable

@Serializable
data class CloudflareZonesResBody(
    val result: List<Zone>,
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)
