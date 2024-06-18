package com.free.tvtracker.data.user

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.user.request.PostFcmTokenApiRequestBody
import com.free.tvtracker.user.request.UpdatePreferencesApiRequestBody
import com.free.tvtracker.user.response.UserApiResponse

class UserRepository(
    private val httpClient: TvHttpClient,
    private val localDataSource: LocalSqlDataProvider,
) {
    suspend fun postFcmToken(token: String): ApiResponse.EmptyApiResponse {
        return try {
            httpClient.call(Endpoints.postFcmToken, PostFcmTokenApiRequestBody(fcmToken = token))
        } catch (e: Throwable) {
            ApiResponse.EmptyApiResponse.error(ApiError.Unknown)
        }
    }

    suspend fun updatePushAllowed(allowed: Boolean): UserApiResponse {
        return try {
            val res = httpClient.call(
                Endpoints.updateUserPreferences,
                UpdatePreferencesApiRequestBody(pushPrefsAllowed = allowed)
            )
            val prefs = localDataSource.getSession()?.copy(preferencesPushAllowed = allowed)
            prefs?.let {
                localDataSource.saveSession(it)
            }
            res
        } catch (e: Throwable) {
            UserApiResponse.error(ApiError.Unknown)
        }
    }
}
