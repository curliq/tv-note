package com.free.tvtracker.core.data.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun getHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Darwin, block)
