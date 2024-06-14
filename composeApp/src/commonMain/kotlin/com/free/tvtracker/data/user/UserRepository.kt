package com.free.tvtracker.data.user

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.core.data.http.TvHttpClient
import com.free.tvtracker.user.request.PostFcmTokenApiRequestBody

class UserRepository(private val httpClient: TvHttpClient) {
    suspend fun postFcmToken(token: String): ApiResponse.EmptyApiResponse {
        return try {
            httpClient.call(Endpoints.postFcmToken, PostFcmTokenApiRequestBody(fcmToken = token))
        } catch (e: Throwable) {
            ApiResponse.EmptyApiResponse.error(ApiError.Unknown)
        }
    }
}
