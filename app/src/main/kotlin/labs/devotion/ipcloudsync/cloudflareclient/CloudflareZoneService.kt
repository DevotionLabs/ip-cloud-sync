package labs.devotion.ipcloudsync.cloudflareclient

import kotlinx.serialization.json.Json
import labs.devotion.ipcloudsync.cloudflareclient.formats.CloudflareZonesResBody
import labs.devotion.ipcloudsync.cloudflareclient.formats.Zone
import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.httpclient.PredefinedHeaders
import labs.devotion.ipcloudsync.httpclient.fromPredefined
import labs.devotion.ipcloudsync.logger.Logger
import okhttp3.Headers

class CloudflareZoneService(
    private val httpClient: HttpClient,
    private val customJson: Json = Json { ignoreUnknownKeys = true }
) {
    fun fetchZoneIdByDomain(domain: String): String {
        Logger.debug("Fetching zone ID for domain: $domain")

        val zones = fetchAllZones()
        Logger.debug("Fetched ${zones.size} zones from Cloudflare")

        val domainZone = zones.find { it.name == domain }
        requireNotNull(domainZone) {
            "No zone found for the domain '$domain'. Ensure the domain exists in your Cloudflare account."
        }

        Logger.info("Found zone ID '${domainZone.id}' for domain '$domain'")
        return domainZone.id
    }

    private fun fetchAllZones(): List<Zone> {
        val endpoint = "/client/v4/zones"
        val acceptJsonHeader = Headers.fromPredefined(PredefinedHeaders.ACCEPT_JSON)

        Logger.debug("Requesting all zones from Cloudflare at endpoint: $endpoint")
        val zonesResponse = httpClient.get(endpoint, acceptJsonHeader)
        Logger.debug("Received response from Cloudflare for zones")

        val decodedZonesResponse = customJson.decodeFromString<CloudflareZonesResBody>(zonesResponse)
        Logger.debug("Successfully decoded Cloudflare zones response")

        return decodedZonesResponse.result
    }
}
