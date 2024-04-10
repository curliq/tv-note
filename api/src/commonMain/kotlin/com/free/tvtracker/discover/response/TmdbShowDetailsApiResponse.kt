package com.free.tvtracker.discover.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TmdbShowDetailsApiResponse(
    override val data: TmdbShowDetailsApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<TmdbShowDetailsApiModel>() {
    companion object {
        fun ok(data: TmdbShowDetailsApiModel): TmdbShowDetailsApiResponse {
            return TmdbShowDetailsApiResponse(data = data)
        }

        fun error(application_error: ApiError): TmdbShowDetailsApiResponse {
            return TmdbShowDetailsApiResponse(application_error = application_error)
        }
    }
}
