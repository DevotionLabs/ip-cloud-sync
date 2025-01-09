package labs.devotion.ipcloudsync.httpclient

import labs.devotion.ipcloudsync.logger.Logger
import okhttp3.Headers
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class HttpClient(
    private val server: String,
    private val token: String? = null
) {
    private val client = OkHttpClient()

    fun get(endpoint: String, headers: Headers = headersOf()): String {
        val url = buildUrl(endpoint)
        Logger.debug("Performing GET request to URL: $url")
        val request = prepareRequest(url, headers).build()

        val response = client.newCall(request).execute()
        return handleResponse(response)
    }

    fun put(endpoint: String, body: String, headers: Headers = headersOf()): String {
        val url = buildUrl(endpoint)
        Logger.debug("Performing PUT request to URL: $url with body: $body")

        val request =
            prepareRequest(url, headers).put(body.toRequestBody("application/json".toMediaTypeOrNull())).build()

        val response = client.newCall(request).execute()
        return handleResponse(response)
    }

    private fun prepareRequest(url: String, headers: Headers = headersOf()): Request.Builder {
        val requestBuilder = Request.Builder().url(url)
        val reqWithHeaders = setHeaders(requestBuilder, headers)

        return reqWithHeaders
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
                Logger.error("HTTP request failed with status code: ${it.code}, body: $body")
                throw IOException("Unexpected code ${it.code} $body")
            }

            if (body.isNullOrEmpty()) {
                return ""
            }

            return body
        }
    }

    private fun buildUrl(endpoint: String): String {
        return "$server$endpoint"
    }
}
