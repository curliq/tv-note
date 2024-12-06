package com.free.tvtracker.expect.data

import com.free.tvtracker.BuildConfig
import com.free.tvtracker.core.data.http.HttpLoggingInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp


actual fun getHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            addInterceptor(loggingInterceptor )
        }
        block()
    }
}

actual fun getServerUrl(): String {
    return BuildConfig.SERVER_URL
}

actual fun getServerPort(): String {
    return BuildConfig.SERVER_PORT
}

actual fun getUserAgent(): String = "android"
