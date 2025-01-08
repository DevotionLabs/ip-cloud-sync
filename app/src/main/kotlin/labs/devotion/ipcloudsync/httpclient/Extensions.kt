package labs.devotion.ipcloudsync.httpclient

import okhttp3.Headers

fun Headers.Companion.fromPredefined(vararg httpHeaders: PredefinedHeaders): Headers {
    val headerStringList = httpHeaders.flatMap { listOf(it.key, it.value) }
    return headersOf(*headerStringList.toTypedArray())
}