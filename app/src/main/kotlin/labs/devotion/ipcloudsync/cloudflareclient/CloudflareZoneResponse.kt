package labs.devotion.ipcloudsync.cloudflareclient

import kotlinx.serialization.Serializable

@Serializable
data class CloudflareZonesResponse(
    val result: List<Zone>,
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)

@Serializable
data class Zone(
    val id: String,
    val name: String
)