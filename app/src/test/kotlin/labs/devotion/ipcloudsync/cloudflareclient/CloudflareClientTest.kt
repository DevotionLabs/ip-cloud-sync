package labs.devotion.ipcloudsync.cloudflareclient

import labs.devotion.ipcloudsync.httpclient.HttpClient
import labs.devotion.ipcloudsync.logger.Logger
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.*

const val domain = "example.com"
const val zoneId = "450adf5f78543a8e568b665ce5c796543"
const val ipAddress = "80.80.80.80"

class CloudflareClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: CloudflareClient

    @BeforeTest
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val serverUrl = "http://${mockWebServer.hostName}:${mockWebServer.port}"
        val httpClient  = HttpClient(serverUrl, "test-token")
        client = CloudflareClient(httpClient)
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test resolveDomainToIp with valid domain`() {
        val mockZoneResponse = mockSuccessZoneResponse()
        mockWebServer.enqueue(mockZoneResponse)

        val mockDnsResponse = mockSuccessDnsResponse()
        mockWebServer.enqueue(mockDnsResponse)

        val resolvedIp = client.resolveDomainToIp(domain)
        assertEquals(ipAddress, resolvedIp)
    }

    @Test
    fun `test resolveDomainToIp with no matching DNS record`() {
        val mockZoneResponse  = mockSuccessZoneResponse()
        mockWebServer.enqueue(mockZoneResponse)

        val mockEmptyDnsResponse = mockEmptyResponse()
        mockWebServer.enqueue(mockEmptyDnsResponse)

        val exception = assertFailsWith<Exception> {
            client.resolveDomainToIp(domain)
        }

        assertEquals("Could not find any DNS record for domain $domain", exception.message)
    }

    @Test
    fun `test resolveDomainToIp with invalid zone`() {
        val mockEmptyZoneResponse = mockEmptyResponse()
        mockWebServer.enqueue(mockEmptyZoneResponse)

        val exception = assertFailsWith<Exception> {
            val ip = client.resolveDomainToIp(domain)
        }

        assertEquals("No zone has been defined for domain $domain", exception.message)
    }

    private fun mockEmptyResponse(): MockResponse {
        val emptyResultBody = """
            {
                "result": [],
                "errors": [],
                "messages": [],
                "success": true
            }
        """.trimIndent()
        return mockSuccessResponse(emptyResultBody)
    }

    private fun mockSuccessZoneResponse(): MockResponse {
        val body = createZoneResponseBody()
        return mockSuccessResponse(body)
    }

    private fun mockSuccessDnsResponse(): MockResponse {
        val body = createDnsResponseBody()
        return mockSuccessResponse(body)
    }

    private fun createZoneResponseBody(): String {
        return """
            {
                "result": [
                    { "id": "$zoneId", "name": "$domain" }
                ],
                "errors": [],
                "messages": [],
                "success": true
            }
        """.trimIndent()
    }

    private fun createDnsResponseBody(): String {
        return """
                {
                    "result": [
                        { "zone_name": "$domain", "content": "$ipAddress" }
                    ],
                "errors": [],
                "messages": [],
                    "success": true
                }
            """.trimIndent()
    }

    private fun mockSuccessResponse(body: String): MockResponse {
        return MockResponse().setResponseCode(200).setBody(body)
    }
}