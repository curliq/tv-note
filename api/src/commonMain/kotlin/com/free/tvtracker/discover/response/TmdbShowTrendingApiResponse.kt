package com.free.tvtracker.discover.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TmdbShowTrendingApiResponse(
    override val data: TrendingShowApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<TrendingShowApiModel>() {
    companion object {
        fun ok(data: TrendingShowApiModel): TmdbShowTrendingApiResponse {
            return TmdbShowTrendingApiResponse(data = data)
        }

        fun error(application_error: ApiError): TmdbShowTrendingApiResponse {
            return TmdbShowTrendingApiResponse(application_error = application_error)
        }
    }
}
