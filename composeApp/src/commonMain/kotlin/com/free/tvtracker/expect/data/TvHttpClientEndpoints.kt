package com.free.tvtracker.expect.data

import com.free.tvtracker.Endpoints
import com.free.tvtracker.data.session.SessionStore
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import com.free.tvtracker.user.response.SessionApiResponse

/**
 * Use this instead of [TvHttpClient] if you need to mock any api response, because
 * [TvHttpClient.call] has reified types which I don't know how to mock.
 * Why not make [TvHttpClient] an interface then? because functions with reified types aren't
 * allowed in interfaces.
 */
class TvHttpClientEndpoints(sessionStore: SessionStore) : TvHttpClient(sessionStore) {
    suspend fun createAnonUser(): SessionApiResponse {
        return call(Endpoints.createAnonUser)
    }

    suspend fun getWatching(): TrackedShowApiResponse {
        return call(Endpoints.getWatching)
    }

    suspend fun getWatchlisted(): TrackedShowApiResponse {
        return call(Endpoints.getWatchlisted)
    }

    suspend fun getFinished(): TrackedShowApiResponse {
        return call(Endpoints.getFinished)
    }
}
