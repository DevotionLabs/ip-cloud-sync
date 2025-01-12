package labs.devotion.ipcloudsync.httpclient

import labs.devotion.ipcloudsync.testutils.TestUtils.createMockHttpClient
import labs.devotion.ipcloudsync.testutils.TestUtils.createMockResponse
import okhttp3.Headers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import java.io.IOException
import kotlin.test.*

class HttpClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: HttpClient

    @BeforeTest
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        client = createMockHttpClient(mockWebServer)
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should obtain a valid response for a GET request`() {
        val exampleIp = "192.168.1.1"

        val mockResponse = createMockResponse(exampleIp)
        mockWebServer.enqueue(mockResponse)

        val actualResponse = client.get("/test-endpoint")

        assertEquals(exampleIp, actualResponse)
    }

    @Test
    fun `should throw an exception for a GET request to a non-existent path`() {
        val mockResponse = createMockResponse("", 404)
        mockWebServer.enqueue(mockResponse)

        assertFailsWith<IOException> {
            client.get("/test-endpoint")
        }
    }

    @Test
    fun `should obtain an empty string when response body is nullish`() {
        val mockResponse = MockResponse().setResponseCode(200) // No body set
        mockWebServer.enqueue(mockResponse)

        val response = client.get("/test-endpoint")

        assertEquals("", response)
    }

    @Test
    fun `should include custom headers in a GET request`() {
        val mockResponse = createMockResponse("Success")
        mockWebServer.enqueue(mockResponse)

        val jsonHeader = PredefinedHeaders.ACCEPT_JSON
        val headers = Headers.fromPredefined(jsonHeader)
        client.get("/test-endpoint", headers)

        val recordedRequest = mockWebServer.takeRequest()

        assertEquals(jsonHeader.value, recordedRequest.getHeader(jsonHeader.key))
    }

    @Test
    fun `should include authorization header if token is provided`() {
        val clientWithToken = createMockHttpClient(mockWebServer, "test-token")
        val mockResponse = createMockResponse("Success")
        mockWebServer.enqueue(mockResponse)

        clientWithToken.get("/test-endpoint")
        val recordedRequest = mockWebServer.takeRequest()

        assertEquals("Bearer test-token", recordedRequest.getHeader("Authorization"))
    }

    @Test
    fun `should send a PUT request with valid JSON body and headers`() {
        val mockResponse = createMockResponse("Updated")
        mockWebServer.enqueue(mockResponse)

        client.put("/update-endpoint", """{"key":"value"}""")

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("""{"key":"value"}""", recordedRequest.body.readUtf8())
    }

    @Test
    fun `should throw IOException for network failure`() {
        val mockResponse = MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockWebServer.enqueue(mockResponse)

        assertFailsWith<IOException> {
            client.get("/test-endpoint")
        }
    }
}
