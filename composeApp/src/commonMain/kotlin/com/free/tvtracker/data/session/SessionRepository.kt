package com.free.tvtracker.data.session

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.user.request.LoginApiRequestBody
import com.free.tvtracker.user.request.PostFcmTokenApiRequestBody
import com.free.tvtracker.user.request.SignupApiRequestBody
import com.free.tvtracker.user.request.UpdatePreferencesApiRequestBody
import com.free.tvtracker.user.response.SessionApiModel
import com.free.tvtracker.user.response.SessionApiResponse
import com.free.tvtracker.user.response.UserApiResponse
import kotlinx.coroutines.flow.Flow

class SessionRepository(
    private val httpClient: TvHttpClientEndpoints,
    private val localDataSource: LocalSqlDataProvider,
    private val sessionStore: SessionStore
) {

    /**
     * @return success
     *
     * Used from the welcome screen
     */
    suspend fun createAnonSession(): Boolean {
        val session = try {
            httpClient.createAnonUser()
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
        if (session?.isSuccess() == true) {
            val res = session.data!!
            try {
                storeSession(res)
            } catch (e: Exception) {
                return false
            }
            sessionStore.token = session.data!!.authToken
            return true
        } else {
            return false
        }
    }

    /**
     * @return success
     *
     * Used from the splash screen
     */
    fun loadSession(): Boolean {
        val storedSession = localDataSource.getSession()
        if (storedSession != null) {
            sessionStore.token = storedSession.token
            return true
        } else {
            return false
        }
    }

    fun geSession(): SessionClientEntity? {
        return localDataSource.getSession()
    }

    fun getSessionFlow(): Flow<SessionClientEntity?> {
        return localDataSource.getSessionFlow()
    }

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

    suspend fun login(username: String, password: String): SessionApiResponse {
        return try {
            val res = httpClient.call(Endpoints.login, LoginApiRequestBody(username, password))
            res.asSuccess {
                storeSession(it)
            }
            res
        } catch (e: Throwable) {
            SessionApiResponse.error(ApiError.Unknown)
        }
    }

    suspend fun signup(username: String, email: String?, password: String): SessionApiResponse {
        return try {
            val res = httpClient.call(Endpoints.postUserCredentials, SignupApiRequestBody(username, password, email))
            res.asSuccess {
                storeSession(it)
            }
            res
        } catch (e: Throwable) {
            SessionApiResponse.error(ApiError.Unknown)
        }
    }

    private fun storeSession(res: SessionApiModel) {
        sessionStore.token = res.authToken
        localDataSource.saveSession(
            SessionClientEntity(
                token = res.authToken,
                createdAtDatetime = res.user.createdAtDatetime,
                userId = res.user.id.toLong(),
                username = res.user.username,
                email = res.user.email,
                preferencesPushAllowed = res.user.preferencesPushAllowed,
                isAnonymous = res.user.isAnonymous,
            )
        )
    }
}
