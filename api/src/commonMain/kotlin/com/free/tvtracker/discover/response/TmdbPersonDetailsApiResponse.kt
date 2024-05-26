package com.free.tvtracker.discover.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class TmdbPersonDetailsApiResponse(
    override val data: TmdbPersonDetailsApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<TmdbPersonDetailsApiModel>() {
    companion object {
        fun ok(data: TmdbPersonDetailsApiModel): TmdbPersonDetailsApiResponse {
            return TmdbPersonDetailsApiResponse(data = data)
        }

        fun error(application_error: ApiError): TmdbPersonDetailsApiResponse {
            return TmdbPersonDetailsApiResponse(application_error = application_error)
        }
    }
}
