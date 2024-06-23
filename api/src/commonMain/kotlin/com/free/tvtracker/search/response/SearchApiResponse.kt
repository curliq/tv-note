package com.free.tvtracker.search.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchApiModel(
    val results: List<SearchMultiApiModel>,
)

@Serializable
data class SearchApiResponse(
    override val data: SearchApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<SearchApiModel>() {
    companion object {
        fun ok(data: SearchApiModel): SearchApiResponse {
            return SearchApiResponse(data = data)
        }

        fun error(application_error: ApiError): SearchApiResponse {
            return SearchApiResponse(application_error = application_error)
        }
    }
}
