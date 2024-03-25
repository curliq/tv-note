package com.free.tvtracker.core.data

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

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
    data class Res<out T>(
        @JsonProperty("error_code")
        val errorCode: Int?,
        @JsonProperty("data")
        val data: T?
    )
}

