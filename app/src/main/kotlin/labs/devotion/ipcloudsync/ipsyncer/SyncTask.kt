package labs.devotion.ipcloudsync.ipsyncer

import labs.devotion.ipcloudsync.cloudflareclient.CloudflareClient
import labs.devotion.ipcloudsync.cloudflareclient.CloudflareDnsService
import labs.devotion.ipcloudsync.cloudflareclient.CloudflareZoneService
import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.ipfetcher.IpFetcher
import labs.devotion.ipcloudsync.logger.Logger

object SyncTask : Runnable {

    private val ipFetcher by lazy {
        val ipFetchServerUrl = Config.getEnv(ConfigKeys.IP_FETCH_SERVER_URL)
        val httpClient = HttpClient(ipFetchServerUrl)

        IpFetcher(httpClient)
    }

    private val cloudflareClient by lazy {
        val cloudflareApiUrl = Config.getEnv(ConfigKeys.CLOUDFLARE_API_URL)
        val cloudflareApiToken = Config.getEnv(ConfigKeys.CLOUDFLARE_API_TOKEN)

        val httpClient = HttpClient(server = cloudflareApiUrl, token = cloudflareApiToken)

        val zoneService = CloudflareZoneService(httpClient)
        val dnsService = CloudflareDnsService(zoneService, httpClient)

        CloudflareClient(dnsService)
    }

    private val domain = Config.getEnv(ConfigKeys.DOMAIN)

    override fun run() {
        Logger.debug("IP sync task started")

        try {
            syncIp()
        } catch (e: Exception) {
            Logger.error("IP Sync process failed: ${e.message}")
        }
    }

    private fun syncIp() {
        val realIp = fetchRealIp()
        val cloudflareIp = fetchCloudflareIp()

        resyncCloudflareIp(realIp = realIp, cloudflareIp = cloudflareIp)
    }

    private fun fetchRealIp(): String {
        Logger.debug("Fetching public IP address provided by ISP")
        return ipFetcher.fetchPublicIp()
    }

    private fun fetchCloudflareIp(): String {
        Logger.debug("Fetching Cloudflare proxy IP address for domain $domain")
        return cloudflareClient.fetchDomainProxiedIp(domain)
    }

    private fun resyncCloudflareIp(realIp: String, cloudflareIp: String) {
        if (realIp == cloudflareIp) {
            Logger.info("The Cloudflare proxy is already resolving to the proper IP address: $realIp")
            return
        }

        Logger.warn("Cloudflare proxy IP ($cloudflareIp) differs from real IP ($realIp). Syncing required.")
        cloudflareClient.updateDomainProxiedIp(domain = domain, newIp = realIp)
    }
}
