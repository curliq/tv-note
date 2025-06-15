package com.free.tvtracker.data.watchlists

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.core.Logger
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
import com.free.tvtracker.watchlists.requests.AddWatchlistApiRequestBody
import com.free.tvtracker.watchlists.requests.DeleteWatchlistApiRequestBody
import com.free.tvtracker.watchlists.requests.GetWatchlistContentApiRequestBody
import com.free.tvtracker.watchlists.requests.RenameWatchlistApiRequestBody
import com.free.tvtracker.watchlists.response.WatchlistsApiResponse
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class WatchlistsRepository(
    private val httpClient: TvHttpClientEndpoints,
    private val logger: Logger,
) {

    companion object {
        val TAG = WatchlistsRepository::class.simpleName!!
    }

    val watchlists = MutableSharedFlow<WatchlistsApiResponse>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    suspend fun fetch(): Flow<WatchlistsApiResponse> {
        val res = try {
            httpClient.call(Endpoints.getWatchlists)
        } catch (e: Exception) {
            logger.e(e, TAG)
            WatchlistsApiResponse.error(ApiError.Network)
        }
        watchlists.emit(res)
        return watchlists
    }

    data class MapContainer(val map: MutableMap<Int, TrackedShowsApiResponse>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is MapContainer) return false

            return map == other.map
        }

        override fun hashCode(): Int {
            return map.hashCode()
        }
    }

    val watchlistsContent: MutableStateFlow<MapContainer> =
        MutableStateFlow(MapContainer(mutableMapOf()))

    suspend fun fetchContent(watchlistId: Int, forceUpdate: Boolean = false) {
        if (watchlistsContent.value.map.containsKey(watchlistId) && !forceUpdate) {
            return
        }
        val res = httpClient.call(Endpoints.getWatchlistContent, GetWatchlistContentApiRequestBody(watchlistId))
        val map = watchlistsContent.value.map
        logger.d(
            "fetch watchlist details, ${map.values.map { it.data?.map { it.tvShow?.storedShow?.backdropImage } }}",
            TAG
        )
        watchlistsContent.update {
            it.copy(map = it.map.plus(mapOf(watchlistId to res)).toMutableMap())
        }
    }

    suspend fun renameList(watchlistId: Int, newName: String) {
        val res = httpClient.call(Endpoints.postWatchlistRename, RenameWatchlistApiRequestBody(watchlistId, newName))
        watchlists.emit(res)
    }

    suspend fun deleteList(watchlistId: Int) {
        val res = httpClient.call(Endpoints.postWatchlistDelete, DeleteWatchlistApiRequestBody(watchlistId))
        watchlists.emit(res)
    }

    suspend fun createList(name: String) {
        val res = httpClient.call(Endpoints.postWatchlistCreate, AddWatchlistApiRequestBody(name))
        watchlists.emit(res)
    }
}
