package labs.devotion.ipcloudsync.cloudflareclient.formats

import kotlinx.serialization.Serializable

@Serializable
data class CloudflareRecordPutReqBody(
    val type: String,
    val name: String,
    val content: String,
    val ttl: Int
)
