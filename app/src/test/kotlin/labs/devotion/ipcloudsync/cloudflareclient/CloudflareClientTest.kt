package labs.devotion.ipcloudsync.cloudflareclient

import labs.devotion.ipcloudsync.httpclient.HttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.*

class CloudflareClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: CloudflareClient

    @BeforeTest
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val serverUrl = mockWebServer.url("/").toString()
        val httpClient = HttpClient(serverUrl, "test-token")
        client = CloudflareClient(httpClient)
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should resolve a domain to a valid IP address`() {
        val successZoneResponse = mockResponse(MockResponses.successZone)
        val successDnsResponse = mockResponse(MockResponses.successDns)

        mockWebServer.enqueue(successZoneResponse)
        mockWebServer.enqueue(successDnsResponse)

        val resolvedIp = client.resolveDomainToIp(domain)
        assertEquals(ipAddress, resolvedIp)
    }

    @Test
    fun `Should throw an error on trying to resolve an unknown domain`() {
        val successZoneResponse = mockResponse(MockResponses.successZone)
        val emptyResultResponse = mockResponse(MockResponses.emptyResult)

        mockWebServer.enqueue(successZoneResponse)
        mockWebServer.enqueue(emptyResultResponse)

        val exception = assertFailsWith<Exception> {
            client.resolveDomainToIp(domain)
        }

        assertEquals("Could not find any DNS record for domain $domain", exception.message)
    }

    @Test
    fun `Should throw an error on trying to resolve a domain for which there is no zone defined`() {
        val emptyResultResponse = mockResponse(MockResponses.emptyResult)
        mockWebServer.enqueue(emptyResultResponse)

        val exception = assertFailsWith<Exception> {
            client.resolveDomainToIp(domain)
        }

        assertEquals("No zone has been defined for domain $domain", exception.message)
    }

    private fun mockResponse(body: String, code: Int = 200): MockResponse {
        return MockResponse().setResponseCode(code).setBody(body)
    }
}
