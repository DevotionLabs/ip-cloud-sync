package labs.devotion.ipcloudsync.ipfetcher

import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.iptools.IpTools

class IpFetcher(private val httpClient: HttpClient) {

    fun fetchPublicIp(): String {
        val ip = httpClient.get("/")
        return IpTools.validateIp(ip)
    }
}
