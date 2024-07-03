package com.free.tvtracker.user.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.Serializable

@Serializable
data class DataExportApiResponse(
    override val data: String? = null,
    override val application_error: ApiError? = null
) : ApiResponse<String>() {

    companion object {
        fun ok(data: String): DataExportApiResponse {
            return DataExportApiResponse(data = data)
        }

        fun error(application_error: ApiError): DataExportApiResponse {
            return DataExportApiResponse(application_error = application_error)
        }
    }
}
