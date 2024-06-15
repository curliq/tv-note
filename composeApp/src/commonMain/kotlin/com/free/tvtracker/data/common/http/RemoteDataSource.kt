package com.free.tvtracker.data.common.http

import com.free.tvtracker.Endpoints
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowApiResponse

class RemoteDataSource(private val tvHttpClient: TvHttpClient) {
    suspend fun getTrackedShows(): TrackedShowApiResponse {
        return tvHttpClient.call(Endpoints.getWatching)
    }

    suspend fun getFinishedShows(): TrackedShowApiResponse {
        return tvHttpClient.call(Endpoints.getFinished)
    }

    suspend fun getWatchlistedShows(): TrackedShowApiResponse {
        return tvHttpClient.call(Endpoints.getWatchlisted)
    }

    suspend fun addTracked(body: AddShowApiRequestBody): AddTrackedShowApiResponse {
        return tvHttpClient.call(Endpoints.addTracked, body)
    }

    suspend fun toggleWatchlist(showId: Int): Boolean {
        return true
        //todo
    }
}
