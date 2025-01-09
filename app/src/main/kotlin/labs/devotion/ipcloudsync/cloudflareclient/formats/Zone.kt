package labs.devotion.ipcloudsync.cloudflareclient.formats

import kotlinx.serialization.Serializable

@Serializable
data class Zone(
    val id: String,
    val name: String
)
