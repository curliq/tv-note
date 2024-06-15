package com.free.tvtracker.data.session

import com.free.tvtracker.Endpoints
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.user.response.UserApiResponse

class SessionRepository(private val httpClient: TvHttpClient) {
    fun token(): String {
        return ""
    }

    suspend fun createAnonymousSession(): UserApiResponse {
        return httpClient.call(Endpoints.createAnonUser)
    }
}
