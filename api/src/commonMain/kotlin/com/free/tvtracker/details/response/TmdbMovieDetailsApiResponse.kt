package com.free.tvtracker.details.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieDetailsApiResponse(
    override val data: TmdbMovieDetailsApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<TmdbMovieDetailsApiModel>() {
    companion object {
        fun ok(data: TmdbMovieDetailsApiModel): TmdbMovieDetailsApiResponse {
            return TmdbMovieDetailsApiResponse(data = data)
        }

        fun error(application_error: ApiError): TmdbMovieDetailsApiResponse {
            return TmdbMovieDetailsApiResponse(application_error = application_error)
        }
    }
}
