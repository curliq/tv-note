package com.free.tvtracker.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

data class ApiResponse<out T>(
    val status: HttpStatus,
    val res: Res<T>,
) : ResponseEntity<Any>(res, status) {
    companion object {
        fun <T> ok(data: T): ApiResponse<T> {
            return ApiResponse(HttpStatus.OK, Res(null, data))
        }

        fun error(errorCode: Int): ApiResponse<Nothing> {
            return ApiResponse(HttpStatus.BAD_REQUEST, Res(errorCode, null))
        }
    }

    @Serializable
    data class Res<out T>(
        @SerialName("error_code")
        val errorCode: Int?,
        @SerialName("data")
        val data: T?
    )
}

@Bean
fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
    return KotlinSerializationJsonHttpMessageConverter(Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    })
}


