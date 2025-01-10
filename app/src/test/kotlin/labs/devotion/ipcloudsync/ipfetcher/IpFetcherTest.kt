package labs.devotion.ipcloudsync.ipfetcher

import labs.devotion.ipcloudsync.iptools.IpTools
import labs.devotion.ipcloudsync.testutils.TestUtils.createMockHttpClient
import labs.devotion.ipcloudsync.testutils.TestUtils.createMockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IpFetcherTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var ipFetcher: IpFetcher

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val httpClient = createMockHttpClient(mockWebServer)
        ipFetcher = IpFetcher(httpClient)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should return a valid IP on fetching it`() {
        val validIp = "192.168.1.1"
        val mockResponse = createMockResponse(validIp)
        mockWebServer.enqueue(mockResponse)

        val ip = ipFetcher.fetchPublicIp()
        assertTrue(IpTools.isValidIp(ip), "Expected a valid IP address, but got: $ip")
    }

    @Test
    fun `should throw an exception on response including an invalid IP`() {
        val invalidIp = "invalid_ip"
        val mockResponse = createMockResponse(invalidIp)
        mockWebServer.enqueue(mockResponse)

        assertFailsWith<IllegalArgumentException> {
            ipFetcher.fetchPublicIp()
        }
    }
}
