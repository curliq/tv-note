package com.free.tvtracker.core.data.http

import com.free.tvtracker.Endpoint
import com.free.tvtracker.base.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun getHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient

class TvHttpClient {

    val localhostAndroid = "10.0.2.2"
    val localhostiOS = "localhost"
    val localhostiOSPhone = "192.168.1.137"
    val tempWifi = "172.16.73.93"

    val cli = getHttpClient {

//        if (OsPlatform().get() != OsPlatform.Platform.Android) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
//        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
        install(DefaultRequest) {
            this.url {
                protocol = URLProtocol.HTTP
                host = localhostiOSPhone
                port = 8080
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            headers {
                append(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzExMTI2MTc1LCJleHAiOjE3MTM3MTgxNzUsInJvbGUiOiJ3YXRjaGVyIn0.9l-cS_0qCRzG1Vgi-JFD5oLVJujNoI84DfkmNq73C4xpAHNaf72D_5f1Gt2whLf9gpRBNnVfuqi2GPeFdwjq9Q"
                )
            }
        }
    }

    inline val client get() = cli

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>, reified BodyType : Any> call(
        endpointType: Endpoint<ReturnType, BodyType>,
        body: BodyType
    ): ReturnType {
        return _makeRequest<ReturnType>(endpointType.verb, endpointType.path, body)
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>> call(
        endpointType: Endpoint<ReturnType, Nothing>,
    ): ReturnType {
        return _makeRequest<ReturnType>(endpointType.verb, endpointType.path, null)
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>> _makeRequest(
        verb: Endpoint.Verb,
        path: String,
        body: Any?
    ): ReturnType {
        return try {
            val res = client.request {
                this.method = when (verb) {
                    Endpoint.Verb.GET -> HttpMethod.Get
                    Endpoint.Verb.POST -> HttpMethod.Post
                }
                url {
                    path(path)
                }
                body?.let {
                    setBody(it)
                }
            }.body<ReturnType>()
            if (res.failedToParse()) {
                throw Exception("Unable to parse response into ReturnType")
            }
            res
        } catch (e: Throwable) {
            throw e
        }
    }
}
