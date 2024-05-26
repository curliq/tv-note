package com.free.tvtracker.discover.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedContentApiResponse(
    override val data: RecommendedContentApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<RecommendedContentApiModel>() {
    companion object {
        fun ok(data: RecommendedContentApiModel): RecommendedContentApiResponse {
            return RecommendedContentApiResponse(data = data)
        }

        fun error(application_error: ApiError): RecommendedContentApiResponse {
            return RecommendedContentApiResponse(application_error = application_error)
        }
    }
}

@Serializable
object ErrorRecommendedNotShowTracked : ApiError("no_tracked_shows")
