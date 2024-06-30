package com.free.tvtracker.discover.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieTrendingApiResponse(
    override val data: TrendingMoviesApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<TrendingMoviesApiModel>() {
    companion object {
        fun ok(data: TrendingMoviesApiModel): TmdbMovieTrendingApiResponse {
            return TmdbMovieTrendingApiResponse(data = data)
        }

        fun error(application_error: ApiError): TmdbMovieTrendingApiResponse {
            return TmdbMovieTrendingApiResponse(application_error = application_error)
        }
    }
}
