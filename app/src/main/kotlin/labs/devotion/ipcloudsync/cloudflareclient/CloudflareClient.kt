package labs.devotion.ipcloudsync.cloudflareclient

import labs.devotion.ipcloudsync.logger.Logger
import labs.devotion.ipcloudsync.validator.NetworkFormatValidator.validateDomain
import labs.devotion.ipcloudsync.validator.NetworkFormatValidator.validateIp

// Facade
class CloudflareClient(
    private val dnsService: CloudflareDnsService
) {
    fun fetchDomainProxiedIp(domain: String): String {
        validateDomain(domain)

        Logger.debug("Fetching proxied IP (DNS record content) for domain: $domain")
        val dnsRecord = dnsService.fetchDnsRecordForDomain(domain)

        Logger.info("Successfully fetched DNS record for domain $domain with content at ${dnsRecord.content}")
        return dnsRecord.content
    }

    fun updateDomainProxiedIp(domain: String, newIp: String) {
        validateDomain(domain)
        validateIp(newIp)

        Logger.debug("Updating DNS record for domain $domain to set content at new IP $newIp")
        dnsService.updateDnsRecordIp(domain, newIp)

        Logger.info("Successfully updated DNS record for domain: $domain to IP: $newIp")
    }
}
