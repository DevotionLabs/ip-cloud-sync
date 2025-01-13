package labs.devotion.ipcloudsync.cloudflareclient

import labs.devotion.ipcloudsync.testutils.TestUtils.createMockHttpClient
import labs.devotion.ipcloudsync.testutils.TestUtils.createMockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.*

class CloudflareClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: CloudflareClient

    @BeforeTest
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val httpClient = createMockHttpClient(mockWebServer, "test-token")
        val zoneService = CloudflareZoneService(httpClient)
        val dnsService = CloudflareDnsService(zoneService, httpClient)

        client = CloudflareClient(dnsService)
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should resolve a domain to a valid IP address`() {
        val successZoneResponse = createMockResponse(MockResponses.successZone)
        val successDnsResponse = createMockResponse(MockResponses.successDns)

        mockWebServer.enqueue(successZoneResponse)
        mockWebServer.enqueue(successDnsResponse)

        val resolvedIp = client.fetchDomainProxiedIp(domain)
        assertEquals(ipAddress, resolvedIp)
    }

    @Test
    fun `should throw an error on trying to resolve an unknown domain`() {
        val successZoneResponse = createMockResponse(MockResponses.successZone)
        val emptyResultResponse = createMockResponse(MockResponses.emptyResult)

        mockWebServer.enqueue(successZoneResponse)
        mockWebServer.enqueue(emptyResultResponse)

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp(domain)
        }
    }

    @Test
    fun `should throw an error on trying to resolve a domain for which there is no zone defined`() {
        val emptyResultResponse = createMockResponse(MockResponses.emptyResult)
        mockWebServer.enqueue(emptyResultResponse)

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp(domain)
        }
    }
}
