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
        client = CloudflareClient(httpClient)
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

        val resolvedIp = client.resolveDomainToIp(domain)
        assertEquals(ipAddress, resolvedIp)
    }

    @Test
    fun `should throw an error on trying to resolve an unknown domain`() {
        val successZoneResponse = createMockResponse(MockResponses.successZone)
        val emptyResultResponse = createMockResponse(MockResponses.emptyResult)

        mockWebServer.enqueue(successZoneResponse)
        mockWebServer.enqueue(emptyResultResponse)

        val exception = assertFailsWith<Exception> {
            client.resolveDomainToIp(domain)
        }

        assertEquals("Could not find any DNS record for domain $domain", exception.message)
    }

    @Test
    fun `should throw an error on trying to resolve a domain for which there is no zone defined`() {
        val emptyResultResponse = createMockResponse(MockResponses.emptyResult)
        mockWebServer.enqueue(emptyResultResponse)

        val exception = assertFailsWith<Exception> {
            client.resolveDomainToIp(domain)
        }

        assertEquals("No zone has been defined for domain $domain", exception.message)
    }
}
