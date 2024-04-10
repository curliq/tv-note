package com.free.tvtracker.core.data.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

actual fun getHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(CIO, block)
}
