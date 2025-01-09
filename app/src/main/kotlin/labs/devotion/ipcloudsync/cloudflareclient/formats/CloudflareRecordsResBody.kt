package labs.devotion.ipcloudsync.cloudflareclient.formats

import kotlinx.serialization.Serializable

@Serializable
data class CloudflareRecordsResBody(
    val result: List<DnsRecords>,
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)
