package labs.devotion.ipcloudsync.testutils

import labs.devotion.ipcloudsync.httpclient.HttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

object TestUtils {
    fun createMockResponse(body: String, code: Int = 200): MockResponse {
        return MockResponse().setResponseCode(code).setBody(body)
    }

    fun createMockHttpClient(mockServer: MockWebServer, token: String? = null): HttpClient {
        val serverUrl = mockServer.url("/").toString()
        return HttpClient(serverUrl, token)
    }
}
