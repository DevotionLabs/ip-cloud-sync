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
        val request = buildGetRequest(endpoint, headers)
        Logger.debug("Performing GET request to URL: ${request.url}")

        val response = executeRequest(request)
        return handleResponse(response)
    }

    fun put(endpoint: String, body: String, headers: Headers = headersOf()): String {
        val request = buildPutRequest(endpoint, body, headers)

        Logger.debug("Performing PUT request to ${request.url} with body: $body")

        val response = executeRequest(request)
        return handleResponse(response)
    }

    private fun buildGetRequest(endpoint: String, headers: Headers = headersOf()): Request {
        val url = buildUrl(endpoint)

        return createRequestBuilder(url, headers).build()
    }

    private fun buildPutRequest(endpoint: String, body: String, headers: Headers = headersOf()): Request {
        val url = buildUrl(endpoint)

        val rawRequest = createRequestBuilder(url, headers)
        val jsonType = "application/json".toMediaTypeOrNull()
        val jsonBody = body.toRequestBody(jsonType)

        return rawRequest.put(jsonBody).build()
    }

    private fun buildUrl(endpoint: String): String {
        return "$server$endpoint"
    }

    private fun createRequestBuilder(url: String, headers: Headers = headersOf()): Request.Builder {
        val requestBuilder = Request.Builder().url(url)
        return setHeaders(requestBuilder, headers)
    }

    private fun setHeaders(builder: Request.Builder, headers: Headers): Request.Builder {
        val builderWithHeaders = builder.headers(headers)

        if (token != null) {
            builderWithHeaders.addHeader("Authorization", "Bearer $token")
        }

        return builderWithHeaders
    }

    private fun executeRequest(request: Request): Response {
        return client.newCall(request).execute()
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
}
