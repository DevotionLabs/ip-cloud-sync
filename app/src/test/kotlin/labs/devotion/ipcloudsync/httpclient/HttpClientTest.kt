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
        val serverUrl = mockWebServer.url("/").toString()
        client = HttpClient(serverUrl)
    }

    @AfterTest
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should obtain a valid response for a GET request`() {
        val exampleIp = "192.168.1.1"

        val mockResponse = MockResponse().setResponseCode(200).setBody(exampleIp)
        mockWebServer.enqueue(mockResponse)

        val response = client.get("/test-endpoint")

        assertEquals(exampleIp, response)
    }

    @Test
    fun `Should throw an exception for a GET request to a non-existent path`() {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        assertFailsWith<IOException> {
            client.get("/test-endpoint")
        }
    }

    @Test
    fun `Should obtain an empty string when response body is nullish`() {
        val mockResponse = MockResponse().setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = client.get("/test-endpoint")

        assertEquals("", response)
    }
}
