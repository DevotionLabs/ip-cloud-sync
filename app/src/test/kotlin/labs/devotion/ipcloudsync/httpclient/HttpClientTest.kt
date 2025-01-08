package labs.devotion.ipcloudsync.httpclient

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
        val serverUrl = "http://${mockWebServer.hostName}:${mockWebServer.port}"
        client = HttpClient(serverUrl)
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test GET request`() {
        val exampleIp = "192.168.1.1"

        val mockResponse = MockResponse().setResponseCode(200).setBody(exampleIp)
        mockWebServer.enqueue(mockResponse)

        val response = client.get("/test-endpoint")

        assertEquals(exampleIp, response)
    }

    @Test
    fun `test GET request with error response`() {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        assertFailsWith<IOException> {
            client.get("/test-endpoint")
        }
    }

    @Test
    fun `test GET request with empty body`() {
        val mockResponse = MockResponse().setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = client.get("/test-endpoint")

        assertEquals("", response)
    }
}
