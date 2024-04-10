package com.free.tvtracker.tracked.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TrackedShowApiResponse(
    override val data: List<TrackedShowApiModel>? = null,
    override val application_error: ApiError? = null
) : ApiResponse<List<TrackedShowApiModel>>() {
    companion object {
        fun ok(data: List<TrackedShowApiModel>): TrackedShowApiResponse {
            return TrackedShowApiResponse(data = data)
        }

        fun error(application_error: ApiError): TrackedShowApiResponse {
            return TrackedShowApiResponse(application_error = application_error)
        }
    }
}
