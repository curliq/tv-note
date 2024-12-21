package com.free.tvtracker.user.response

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionApiModel(
    @SerialName("token")
    val authToken: String,
    @SerialName("user")
    val user: UserApiModel,
)

@Serializable
data class SessionApiResponse(
    override val data: SessionApiModel? = null,
    override val application_error: ApiError? = null
) : ApiResponse<SessionApiModel>() {

    companion object {
        fun ok(data: SessionApiModel): SessionApiResponse {
            return SessionApiResponse(data = data)
        }

        fun error(application_error: ApiError): SessionApiResponse {
            return SessionApiResponse(application_error = application_error)
        }
    }
}

@Serializable
object ErrorInvalidCredentials : ApiError("invalid_creds")

@Serializable
object ErrorAccountAlreadyExists : ApiError("account_already_exists")

@Serializable
object ErrorMissingCreds : ApiError("error_missing_creds")
