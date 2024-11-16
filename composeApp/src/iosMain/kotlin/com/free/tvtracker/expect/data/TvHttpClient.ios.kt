package com.free.tvtracker.expect.data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.dictionaryWithContentsOfFile

actual fun getHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Darwin, block)

actual fun getServerUrl(): String {
    val secrets = NSBundle.mainBundle.pathForResource("Secrets", "plist") ?:""
    return NSDictionary.dictionaryWithContentsOfFile(secrets)?.get("SERVER_URL") as? String ?: ""
}

actual fun getServerPort(): String {
    val secrets = NSBundle.mainBundle.pathForResource("Secrets", "plist") ?:""
    return NSDictionary.dictionaryWithContentsOfFile(secrets)?.get("SERVER_PORT") as? String ?: ""
}
