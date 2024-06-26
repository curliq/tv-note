package com.free.tvtracker.tracked.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TrackedShowsApiResponse(
    override val data: List<TrackedContentApiModel>? = null,
    override val application_error: ApiError? = null
) : ApiResponse<List<TrackedContentApiModel>>() {
    companion object {
        fun ok(data: List<TrackedContentApiModel>): TrackedShowsApiResponse {
            return TrackedShowsApiResponse(data = data)
        }

        fun error(application_error: ApiError): TrackedShowsApiResponse {
            return TrackedShowsApiResponse(application_error = application_error)
        }
    }
}
