package com.free.tvtracker.data.tracked

import com.benasher44.uuid.uuid4
import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.core.data.http.TvHttpClient
import com.free.tvtracker.core.data.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach

class TrackedShowsRepository(
    private val remoteDataSource: TvHttpClient,
    private val localDataSource: LocalSqlDataProvider,
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue
) {

    private var last: TrackedShowApiResponse? = null
    private val _watchingShows: MutableSharedFlow<TrackedShowApiResponse> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * Persistent data source to be consumed by multiple ViewModels (ie watching screen and search screen)
     */
    val watchingShows: Flow<TrackedShowApiResponse> = _watchingShows
        .onEach { response -> last = response }

    suspend fun emitLatest() {
        watchedEpisodesTaskQueue.emitOrders()
        val localWatching = localDataSource.getTrackedShows().map { it.toApiModel() }
        if (localWatching.isNotEmpty()) {
            _watchingShows.tryEmit(TrackedShowApiResponse.ok(localWatching))
        }
        val res = try {
            val res = remoteDataSource.call(Endpoints.watching)
            res.asSuccess { data ->
                localDataSource.saveTrackedShow(data.map { it.toClientEntity() })
            }
            res
        } catch (e: Exception) {
            TrackedShowApiResponse.error(ApiError.Network)
        }
        if (res.isSuccess() || localWatching.isEmpty()) {
            _watchingShows.tryEmit(res)
        }
    }

    suspend fun getOrUpdateWatchingShows(): TrackedShowApiResponse {
        if (last != null) {
            return last!!
        }
        emitLatest()
        return last!!
    }

    suspend fun markEpisodeAsWatched(showId: Int, episodeId: String) {
        val order = MarkEpisodeWatchedOrderClientEntity(uuid4().toString(), showId.toLong(), episodeId)
        watchedEpisodesTaskQueue.add(order)
    }

    suspend fun addTrackedShow(showId: Int) {
        val res = remoteDataSource.call(Endpoints.addTracked, AddShowRequest(showId, false))
        res.coAsSuccess { newShow ->
            val combined = if (last != null && last!!.isSuccess()) {
                last!!.copy(data = last!!.data!!.plus(newShow))
            } else {
                TrackedShowApiResponse.ok(listOf(newShow))
            }
            _watchingShows.tryEmit(combined)
        }
    }
}
