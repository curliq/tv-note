package com.free.tvtracker.watchlists.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistsApiResponse(
    override val data: List<WatchlistApiModel>? = null,
    override val application_error: ApiError? = null
) : ApiResponse<List<WatchlistApiModel>>() {
    companion object {
        fun ok(data: List<WatchlistApiModel>): WatchlistsApiResponse {
            return WatchlistsApiResponse(data = data)
        }

        fun error(application_error: ApiError): WatchlistsApiResponse {
            return WatchlistsApiResponse(application_error = application_error)
        }
    }
}
