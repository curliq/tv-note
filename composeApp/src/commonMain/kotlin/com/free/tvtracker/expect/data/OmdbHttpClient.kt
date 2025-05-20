package com.free.tvtracker.expect.data

import com.free.tvtracker.base.ApiResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun getOmdbToken(): String

class OmdbHttpClient() {

    inline val client get() = cli()
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
                host = "www.omdbapi.com"
                parameters.append("apikey", getOmdbToken())
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType> getRatings(
        imdbId: String
    ): ReturnType {
        return try {
            val res = client.request {
                this.method = HttpMethod.Get
                this.parameter("i", imdbId)
            }.body<ReturnType>()
            res
        } catch (e: Throwable) {
            throw e
        }
    }
}
