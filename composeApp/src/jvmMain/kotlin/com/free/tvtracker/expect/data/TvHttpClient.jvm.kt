package com.free.tvtracker.expect.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

actual fun getHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(CIO, block)
}

actual fun getServerUrl(): String = ""

actual fun getServerPort(): String = ""
