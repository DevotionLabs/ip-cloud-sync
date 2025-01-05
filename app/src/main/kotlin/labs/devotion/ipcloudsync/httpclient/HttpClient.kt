package labs.devotion.ipcloudsync.httpclient

import okhttp3.*
import java.io.IOException

class HttpClient(
    private val protocol: Protocol, private val server: String
) {
    private val client = OkHttpClient()

    fun get(endpoint: String): String {
        val url = buildUrl(endpoint)
        val requestBuilder = Request.Builder().url(url)

        val request = requestBuilder.build()

        val response = client.newCall(request).execute()
        return handleResponse(response)
    }

    private fun handleResponse(response: Response): String {
        response.use {
            val body = it.body?.string()

            if (!it.isSuccessful) {
                throw IOException("Unexpected code ${it.code} $body")
            }

            return body ?: throw IOException("Response body is empty")
        }
    }

    private fun buildUrl(endpoint: String): String {
        return "${protocol}${server}${endpoint}"
    }
}
