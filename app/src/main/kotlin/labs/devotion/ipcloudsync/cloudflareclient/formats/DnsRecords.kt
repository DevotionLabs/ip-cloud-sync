package labs.devotion.ipcloudsync.cloudflareclient.formats

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class DnsRecords(
    val id: String,
    val zone_name: String,
    val content: String
)
