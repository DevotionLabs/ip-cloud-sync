package labs.devotion.ipcloudsync.cloudflareclient

import kotlinx.serialization.json.Json
import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.httpclient.PredefinedHeaders
import labs.devotion.ipcloudsync.httpclient.Protocol
import labs.devotion.ipcloudsync.httpclient.fromPredefined
import okhttp3.Headers

const val CLOUDFLARE_API_URL = "api.cloudflare.com"

class CloudflareClient(token: String?) {

    private val httpClient = HttpClient(Protocol.HTTPS, CLOUDFLARE_API_URL, token)

    private val customJson = Json {
        ignoreUnknownKeys = true // Avoid failure on omitted keys
    }

    fun resolveDomainToIp(domain: String): String {
        val zoneId = fetchZoneIdByDomain(domain)
        return fetchDnsRecordForDomain(zoneId = zoneId, domain = domain)
    }

    private fun fetchZoneIdByDomain(domain: String): String {
        val zones = fetchZones()

        val domainZone = zones.find { it.name == domain }

        if (domainZone == null) {
            throw Exception("No zone has been defined for domain $domain")
        }

        return domainZone.id
    }

    private fun fetchZones(): List<Zone> {
        val endpoint = "/client/v4/zones"
        val zonesResponse = httpClient.get(endpoint, Headers.fromPredefined(PredefinedHeaders.ACCEPT_JSON))
        val decodedZonesResponse = customJson.decodeFromString<CloudflareZonesResponse>(zonesResponse)

        return decodedZonesResponse.result
    }

    private fun fetchDnsRecordForDomain(zoneId: String, domain: String): String {
        val dnsRecords = fetchDnsRecords(zoneId)

        val record = dnsRecords.find { it.zone_name == domain }

        if (record == null) {
            throw Exception("Could not find any DNS record for domain \$domain")
        }

        return record.content
    }

    private fun fetchDnsRecords(zoneId: String): List<DnsRecords> {
        val endpoint = "/client/v4/zones/$zoneId/dns_records?type=A"
        val dnsRecordsResult = httpClient.get(endpoint, Headers.fromPredefined(PredefinedHeaders.ACCEPT_JSON))
        val dnsRecords = customJson.decodeFromString<CloudflareDnsRecordsResponse>(dnsRecordsResult)

        return dnsRecords.result
    }

    fun updateDomainIp() {
        // TODO
    }
}