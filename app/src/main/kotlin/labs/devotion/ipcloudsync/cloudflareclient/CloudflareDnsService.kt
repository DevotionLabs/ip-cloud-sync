package labs.devotion.ipcloudsync.cloudflareclient

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import labs.devotion.ipcloudsync.cloudflareclient.formats.CloudflareRecordPutReqBody
import labs.devotion.ipcloudsync.cloudflareclient.formats.CloudflareRecordsResBody
import labs.devotion.ipcloudsync.cloudflareclient.formats.DnsRecord
import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.httpclient.PredefinedHeaders
import labs.devotion.ipcloudsync.httpclient.fromPredefined
import labs.devotion.ipcloudsync.logger.Logger
import okhttp3.Headers

class CloudflareDnsService(
    private val zoneService: CloudflareZoneService,
    private val httpClient: HttpClient,
    private val customJson: Json = Json {
        ignoreUnknownKeys = true
    }
) {
    fun fetchDnsRecordForDomain(domain: String): DnsRecord {
        Logger.debug("Fetching DNS record for domain: $domain")
        val zoneId = zoneService.fetchZoneIdByDomain(domain)

        val dnsRecord = fetchDnsRecordByZoneId(zoneId, domain)
        Logger.info("Fetched DNS record for domain: $domain with content: ${dnsRecord.content}")
        return dnsRecord
    }

    fun updateDnsRecordIp(newIp: String, domain: String, ttl: Int = 1) {
        Logger.debug("Starting update of DNS record for domain: $domain to new IP: $newIp with TTL: $ttl")

        val record = fetchDnsRecordForDomain(domain)
        Logger.debug("Fetched DNS record for update: ${record.id} in zone: ${record.zone_id}")

        val endpoint = "/client/v4/zones/${record.zone_id}/dns_records/${record.id}"
        val updateRequestBody = CloudflareRecordPutReqBody(
            name = domain,
            type = "A",
            ttl = ttl,
            content = newIp
        )

        httpClient.put(endpoint, customJson.encodeToString(updateRequestBody))
        Logger.info("Successfully updated DNS record for domain: $domain to IP: $newIp")
    }

    private fun fetchDnsRecordByZoneId(zoneId: String, domain: String): DnsRecord {
        Logger.debug("Fetching DNS records for zone ID: $zoneId")
        val allRecords = fetchAllZoneDnsRecords(zoneId)

        val dnsRecord = findDomainRecord(allRecords, domain)
        Logger.info("Found DNS record for domain: $domain in zone ID: $zoneId")
        return dnsRecord
    }

    private fun fetchAllZoneDnsRecords(zoneId: String): List<DnsRecord> {
        Logger.debug("Requesting all DNS records for zone ID: $zoneId")
        val endpoint = "/client/v4/zones/$zoneId/dns_records?type=A"
        val acceptJsonHeader = Headers.fromPredefined(PredefinedHeaders.ACCEPT_JSON)

        val dnsRecordsResult = httpClient.get(endpoint, acceptJsonHeader)

        val dnsRecords = customJson.decodeFromString<CloudflareRecordsResBody>(dnsRecordsResult)
        Logger.info("Decoded ${dnsRecords.result.size} DNS records from response for zone ID: $zoneId")
        return dnsRecords.result
    }

    private fun findDomainRecord(dnsRecords: List<DnsRecord>, domain: String): DnsRecord {
        Logger.debug("Searching for DNS record for domain: $domain in the provided records")
        val record = dnsRecords.find { it.zone_name == domain }

        requireNotNull(record) { "Could not find any DNS record for domain $domain" }

        Logger.info("Found DNS record for domain: $domain with ID: ${record.id}")
        return record
    }
}
