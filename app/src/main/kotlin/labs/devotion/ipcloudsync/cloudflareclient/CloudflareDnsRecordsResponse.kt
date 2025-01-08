package labs.devotion.ipcloudsync.cloudflareclient

import kotlinx.serialization.Serializable

@Serializable
data class CloudflareDnsRecordsResponse(
    val result: List<DnsRecords>,
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)

@Serializable
data class DnsRecords(
    val zone_name: String,
    val content: String
)