package labs.devotion.ipcloudsync.cloudflareclient

import kotlinx.serialization.json.Json
import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.httpclient.PredefinedHeaders
import labs.devotion.ipcloudsync.httpclient.fromPredefined
import labs.devotion.ipcloudsync.logger.Logger
import okhttp3.Headers

class CloudflareClient(private val httpClient: HttpClient) {

    private val customJson = Json {
        ignoreUnknownKeys = true // Avoid failure on omitted keys
    }

    fun resolveDomainToIp(domain: String): String {
        Logger.debug("Resolving domain to IP: $domain")
        val zoneId = fetchZoneIdByDomain(domain)

        Logger.info("Found zone ID for domain $domain: $zoneId")
        return fetchDnsRecordForDomain(zoneId = zoneId, domain = domain)
    }

    private fun fetchZoneIdByDomain(domain: String): String {
        Logger.debug("Fetching zones for domain: $domain")
        val zones = fetchZones()

        val domainZone = zones.find { it.name == domain }
        requireNotNull(domainZone) { "No zone has been defined for domain $domain" }

        return domainZone.id
    }

    private fun fetchZones(): List<Zone> {
        Logger.debug("Fetching all zones from Cloudflare")
        val endpoint = "/client/v4/zones"
        val zonesResponse = httpClient.get(endpoint, Headers.fromPredefined(PredefinedHeaders.ACCEPT_JSON))
        val decodedZonesResponse = customJson.decodeFromString<CloudflareZonesResponse>(zonesResponse)

        return decodedZonesResponse.result
    }

    private fun fetchDnsRecordForDomain(zoneId: String, domain: String): String {
        Logger.debug("Fetching DNS records for domain: $domain in zone: $zoneId")
        val dnsRecords = fetchDnsRecords(zoneId)

        val record = dnsRecords.find { it.zone_name == domain }
        requireNotNull(record) { "Could not find any DNS record for domain $domain" }

        return record.content
    }

    private fun fetchDnsRecords(zoneId: String): List<DnsRecords> {
        Logger.debug("Fetching DNS records for zone ID: $zoneId")
        val endpoint = "/client/v4/zones/$zoneId/dns_records?type=A"
        val dnsRecordsResult = httpClient.get(endpoint, Headers.fromPredefined(PredefinedHeaders.ACCEPT_JSON))
        val dnsRecords = customJson.decodeFromString<CloudflareDnsRecordsResponse>(dnsRecordsResult)

        return dnsRecords.result
    }

    fun updateDomainIp() {
        // TODO
    }
}