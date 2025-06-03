package com.free.tvtracker.data.watchlists

import com.free.tvtracker.Endpoints
import com.free.tvtracker.core.Logger
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
import com.free.tvtracker.watchlists.requests.DeleteWatchlistApiRequestBody
import com.free.tvtracker.watchlists.requests.GetWatchlistContentApiRequestBody
import com.free.tvtracker.watchlists.requests.RenameWatchlistApiRequestBody
import com.free.tvtracker.watchlists.response.WatchlistsApiResponse
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class WatchlistsRepository(
    private val httpClient: TvHttpClientEndpoints,
    private val logger: Logger,
) {

    val watchlists = MutableSharedFlow<WatchlistsApiResponse>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    suspend fun fetch(): Flow<WatchlistsApiResponse> {
        val res = httpClient.call(Endpoints.getWatchlists)
        watchlists.emit(res)
        return watchlists
    }

    private var watchlistsContentCache: MutableMap<Int, TrackedShowsApiResponse> = mutableMapOf()

    suspend fun fetchContent(watchlistId: Int): TrackedShowsApiResponse {
        if (watchlistsContentCache.contains(watchlistId)) {
            return watchlistsContentCache[watchlistId]!!
        }
        val res = httpClient.call(Endpoints.getWatchlistContent, GetWatchlistContentApiRequestBody(watchlistId))
        watchlistsContentCache.put(watchlistId, res)
        return res
    }

    suspend fun renameList(watchlistId: Int, newName: String) {
        val res = httpClient.call(Endpoints.postWatchlistRename, RenameWatchlistApiRequestBody(watchlistId, newName))
        watchlists.emit(res)
    }

    suspend fun deleteList(watchlistId: Int) {
        val res = httpClient.call(Endpoints.postWatchlistDelete, DeleteWatchlistApiRequestBody(watchlistId))
        watchlists.emit(res)
    }
}

