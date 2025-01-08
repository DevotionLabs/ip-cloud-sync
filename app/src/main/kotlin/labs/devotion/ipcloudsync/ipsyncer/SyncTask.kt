package labs.devotion.ipcloudsync.ipsyncer

import labs.devotion.ipcloudsync.cloudflareclient.CloudflareClient
import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.ipfetcher.IpFetcher
import labs.devotion.ipcloudsync.logger.Logger

object SyncTask : Runnable {
    override fun run() {
        val realIp = fetchRealIp()
        val cloudflareIp = fetchCloudflareIp()

        resyncCloudflareIp(realIp = realIp, cloudflareIp = cloudflareIp)
    }

    private fun fetchRealIp(): String {
        val ipFetchServerUrl = Config.getEnv(ConfigKeys.IP_FETCH_SERVER_URL)
        val httpClient = HttpClient(ipFetchServerUrl)
        val ipFetcher = IpFetcher(httpClient)
        return ipFetcher.fetchPublicIp()
    }

    private fun fetchCloudflareIp(): String {
        val cloudflareApiUrl = Config.getEnv(ConfigKeys.CLOUDFLARE_API_URL)
        val cloudflareApiToken = Config.getEnv(ConfigKeys.CLOUDFLARE_API_TOKEN)
        val domain = Config.getEnv(ConfigKeys.DOMAIN)

        val httpClient = HttpClient(server = cloudflareApiUrl, token = cloudflareApiToken)
        val cloudflareClient = CloudflareClient(httpClient)
        return cloudflareClient.resolveDomainToIp(domain)
    }

    private fun resyncCloudflareIp(realIp: String, cloudflareIp: String) {
        if (realIp == cloudflareIp) {
            Logger.info("The Cloudflare proxy is already resolving to the proper IP address")
            return
        }

        Logger.warning("The Cloudflare proxy resolves to a wrong IP address")
    }
}