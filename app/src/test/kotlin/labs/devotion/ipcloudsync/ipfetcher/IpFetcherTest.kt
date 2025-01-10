package labs.devotion.ipcloudsync.ipfetcher

import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.iptools.IpTools
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IpFetcherTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var httpClient: HttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val serverUrl = "http://${mockWebServer.hostName}:${mockWebServer.port}"
        httpClient = HttpClient(serverUrl)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should return a valid IP on fetching it`() {
        val validIp = "192.168.1.1"
        val mockResponse = MockResponse().setResponseCode(200).setBody(validIp)
        mockWebServer.enqueue(mockResponse)

        val ip = IpFetcher(httpClient).fetchPublicIp()
        assertTrue(IpTools.isValidIp(ip), "Expected a valid IP address, but got: $ip")
    }

    @Test
    fun `Should throw an exception on response including an invalid IP`() {
        val invalidIp = "invalid_ip"
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(invalidIp))

        val ipFetcher = IpFetcher(httpClient)
        assertFailsWith<IllegalArgumentException> {
            ipFetcher.fetchPublicIp()
        }
    }
}
