package com.free.tvtracker.tmdb

import com.free.tvtracker.logging.OutRequestLoggingInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@ConfigurationProperties("environment.secrets")
data class SecretProperties(
    val tmdbKey: String,
)

@Configuration
class RestTemplateConfig {
    /**
     * Used so that sentry can add its interceptor to trace outward requests
     */
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()
}

@Component
@EnableConfigurationProperties(SecretProperties::class)
class TmdbClient(
    private val logger: OutRequestLoggingInterceptor,
    private val prop: SecretProperties,
    private val restTemplate: RestTemplate
) {

    private fun buildClient(): RestTemplate {
        if (logger.get.isDebugEnabled) {
            restTemplate.requestFactory = BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory())
            restTemplate.interceptors = restTemplate.interceptors.plus(logger)
        }
        return restTemplate
    }

    private val client: RestTemplate = buildClient()

    private fun buildHeaders(): HttpEntity<String> {
        val headers = HttpHeaders()
        headers.set("accept", "application/json")
        headers.set("Authorization", "Bearer ${prop.tmdbKey}")
        return HttpEntity("", headers)
    }

    fun <T> get(endpoint: String, returnType: Class<T>, params: Map<String, Any> = emptyMap()): ResponseEntity<T> {
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
