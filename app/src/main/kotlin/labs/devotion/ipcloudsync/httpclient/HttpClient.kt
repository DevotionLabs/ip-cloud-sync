package labs.devotion.ipcloudsync.httpclient

import okhttp3.*
import okhttp3.Headers.Companion.headersOf
import java.io.IOException

class HttpClient(
    private val protocol: Protocol, private val server: String, private val token: String? = null
) {
    private val client = OkHttpClient()

    fun get(endpoint: String, headers: Headers = headersOf()): String {
        val url = buildUrl(endpoint)
        val request = buildRequest(url, headers)

        val response = client.newCall(request).execute()
        return handleResponse(response)
    }

    private fun buildRequest(url: String, headers: Headers = headersOf()): Request {
        val requestBuilder = Request.Builder().url(url)
        val reqWithHeaders = setHeaders(requestBuilder, headers)

        return reqWithHeaders.build()
    }

    private fun setHeaders(builder: Request.Builder, headers: Headers): Request.Builder {
        val builderWithHeaders = builder.headers(headers)

        if (token != null) {
            builderWithHeaders.addHeader("Authorization", "Bearer $token")
        }

        return builderWithHeaders
    }

    private fun handleResponse(response: Response): String {
        response.use {
            val body = it.body?.string()

            if (!it.isSuccessful) {
                throw IOException("Unexpected code ${it.code} $body")
            }

            if (body.isNullOrEmpty()){
                return ""
            }

            return body
        }
    }

    private fun buildUrl(endpoint: String): String {
        return "${protocol}${server}${endpoint}"
    }
}
