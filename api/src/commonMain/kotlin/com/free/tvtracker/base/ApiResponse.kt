package com.free.tvtracker.base

import kotlinx.serialization.Serializable

/**
 * base class for all endpoints json response
 */
@Serializable
abstract class ApiResponse<T> {
    abstract val data: T?

    /**
     * Why snake case? Because @SerialName is not supported in abstract classes,
     * support to be added here: https://github.com/Kotlin/kotlinx.serialization/issues/1950#issuecomment-1362986792
     */
    abstract val application_error: ApiError?

    fun isSuccess(): Boolean {
        return data != null
    }

    fun isError(): Boolean {
        return data == null
    }

    fun failedToParse(): Boolean {
        return data == null && application_error == null
    }

    suspend fun coAsSuccess(run: suspend (data: T) -> Unit): ApiResponse<T> {
        if (isSuccess()) {
            run(data!!)
        }
        return this
    }

    fun asSuccess(run: (data: T) -> Unit): ApiResponse<T> {
        if (isSuccess()) {
            run(data!!)
        }
        return this
    }


    suspend fun coAsError(run: suspend (error: ApiError) -> Unit): ApiResponse<T> {
        if (!isSuccess()) {
            run(application_error!!)
        }
        return this
    }

    fun asError(run: (error: ApiError) -> Unit): ApiResponse<T> {
        if (!isSuccess()) {
            run(application_error!!)
        }
        return this
    }

    @Serializable
    class EmptyApiResponse(override val data: Int?, override val application_error: ApiError?) : ApiResponse<Int>() {
        companion object {
            fun ok(): EmptyApiResponse {
                return EmptyApiResponse(data = 0, application_error = null)
            }

            fun error(application_error: ApiError): EmptyApiResponse {
                return EmptyApiResponse(data = null, application_error = application_error)
            }
        }
    }
}

@Serializable
open class ApiError(open val code: String) {
    @Serializable
    object Unknown : ApiError("no_lo_se")

    @Serializable
    object Network : ApiError("network_error")

    @Serializable
    object Cancelled : ApiError("canceled")
}
