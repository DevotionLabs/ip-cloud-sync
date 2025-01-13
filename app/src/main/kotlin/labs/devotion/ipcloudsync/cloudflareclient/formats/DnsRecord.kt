package labs.devotion.ipcloudsync.cloudflareclient.formats

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class DnsRecord(
    val id: String,
    val zone_id: String,
    val zone_name: String,
    val content: String
)
