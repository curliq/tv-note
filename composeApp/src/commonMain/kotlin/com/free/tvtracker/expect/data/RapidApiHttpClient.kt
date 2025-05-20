package com.free.tvtracker.expect.data

import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun getRapidApiToken(): String

class RapidApiHttpClient() {

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
                protocol = URLProtocol.HTTPS
                host = "imdb232.p.rapidapi.com"
                header("X-RapidAPI-Key", getRapidApiToken())
                header("X-RapidAPI-Host", "imdb232.p.rapidapi.com")
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    @Throws(Throwable::class)
    suspend inline fun <reified ReturnType> getReviews(
        imdbId: String
    ): ReturnType {
        return try {
            val res = client.request {
                this.method = HttpMethod.Get
                this.parameter("i", imdbId)
                url("api/title/get-user-reviews?order=DESC&spoiler=EXCLUDE&tt=$imdbId&sortBy=HELPFULNESS_SCORE")
            }.body<ReturnType>()
            res
        } catch (e: Throwable) {
            throw e
        }
    }
}
