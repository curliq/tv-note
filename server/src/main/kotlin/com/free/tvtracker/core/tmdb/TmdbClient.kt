package com.free.tvtracker.core.tmdb

import com.free.tvtracker.core.logging.OutRequestLoggingInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@ConfigurationProperties("env-secrets")
data class SecretProperties(
    val tmdbKey: String,
)

@Component
@EnableConfigurationProperties(SecretProperties::class)
class TmdbClient(private val logger: OutRequestLoggingInterceptor, private val prop: SecretProperties) {

    private fun buildClient(): RestTemplate {
        val client = RestTemplate()
        if (logger.get.isDebugEnabled) {
            client.requestFactory = BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory())
            client.interceptors = client.interceptors.plus(logger)

        }
        return client
    }

    private val client: RestTemplate = buildClient()

    private fun buildHeaders(): HttpEntity<String> {
        val headers = HttpHeaders()
        headers.set("accept", "application/json")
        headers.set("Authorization", "Bearer ${prop.tmdbKey}")
        return HttpEntity("", headers)
    }

    fun <T> get(endpoint: String, returnType: Class<T>, params: Map<String, String> = emptyMap()): ResponseEntity<T> {
//        return HttpClient(CIO) {
//            install(ContentNegotiation) {
//                json(Json {
//                    prettyPrint = true
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                })
//            }
//            install(Logging) {
//                logger = Logger.DEFAULT
//                level = LogLevel.ALL
//                logger = object: Logger {
//                    override fun log(message: String) {
//                        println("HTTP Client $message")
//                    }
//                }
//            }
//            install(DefaultRequest) {
//                this.url {
//                    protocol = URLProtocol.HTTPS
//                    host = "api.themoviedb.org"
//                }
//                contentType(ContentType.Application.Json)
//                accept(ContentType.Application.Json)
//                headers {
//                    append(
//                        "Authorization",
//                        "Bearer ${prop.tmdbKey}"
//                    )
//                }
//            }
//        }.request {
//            this.method = Get
//            url {
//                path(endpoint)
////                path(path)
//                params.forEach { (name, value) ->
//                    parameters.append(name=name, value= value)
//                }
//            }
////            body?.let {
////                setBody(it)
////            }
//        }.body(TypeInfo(returnType::class, returnType))

        val builder = UriComponentsBuilder.fromUriString("https://api.themoviedb.org$endpoint")
            .apply {
                params.forEach { (t, u) ->
                    queryParam(t, u)
                }
            }
        return client.exchange(
            builder.build().toUriString(),
            HttpMethod.GET,
            buildHeaders(),
            returnType,
        )
    }
}
