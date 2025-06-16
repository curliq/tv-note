package com.free.tvtracker.expect.data

import com.free.tvtracker.Endpoint
import com.free.tvtracker.EndpointNoBody
import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.data.session.SessionStore
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
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
expect fun getServerUrl(): String
expect fun getServerPort(): String
expect fun getUserAgent(): String

open class TvHttpClient(private val sessionStore: SessionStore) {

    private val localhostiOS = "localhost:${getServerPort()}"
    private val localhostiOSPhone = "192.168.1.205:8080"
    private val server: String by lazy { "${getServerUrl()}:${getServerPort()}" }

    fun cli() = getHttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
            connectTimeoutMillis = 5000
            socketTimeoutMillis = 5000
        }
        install(DefaultRequest) {
            this.url {
                protocol = URLProtocol.HTTP
                host = server
//                host = localhostiOSPhone
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }.apply {
        this.plugin(HttpSend).intercept { request ->
            request.headers {
                sessionStore.token?.let {
                    append(
                        "Authorization",
                        "Bearer $it"
                    )
                }
                append("User-Agent", "ktor-client-${getUserAgent()}")
            }
            execute(request)
        }
    }

    inline val client get() = cli()

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>, reified BodyType : Any> call(
        endpointType: Endpoint<ReturnType, BodyType>,
        body: BodyType,
    ): ReturnType {
        return _makeRequest<ReturnType>(endpointType.verb, endpointType.path, body = body)
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>> call(
        endpointType: EndpointNoBody<ReturnType>,
    ): ReturnType {
        return _makeRequest<ReturnType>(endpointType.verb, endpointType.path, body = null)
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType : ApiResponse<out Any>> _makeRequest(
        verb: Endpoint.Verb,
        path: String,
        body: Any?,
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
