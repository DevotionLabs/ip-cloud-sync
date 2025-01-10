package labs.devotion.ipcloudsync.httpclient

import labs.devotion.ipcloudsync.testutils.TestUtils.createMockHttpClient
import labs.devotion.ipcloudsync.testutils.TestUtils.createMockResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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
        val mockResponse = MockResponse().setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = client.get("/test-endpoint")

        assertEquals("", response)
    }
}
