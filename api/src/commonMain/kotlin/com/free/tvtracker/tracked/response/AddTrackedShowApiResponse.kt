package com.free.tvtracker.tracked.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class AddTrackedShowApiResponse(
    override val data: TrackedContentApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<TrackedContentApiModel>() {
    companion object {
        fun ok(data: TrackedContentApiModel): AddTrackedShowApiResponse {
            return AddTrackedShowApiResponse(data = data)
        }

        fun error(application_error: ApiError): AddTrackedShowApiResponse {
            return AddTrackedShowApiResponse(application_error = application_error)
        }
    }
}

@Serializable
object ErrorShowAlreadyAdded : ApiError("already_added")

@Serializable
object ErrorShowIsGone : ApiError("show_gone")
