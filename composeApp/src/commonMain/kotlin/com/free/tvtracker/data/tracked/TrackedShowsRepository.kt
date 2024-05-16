package com.free.tvtracker.data.tracked

import com.benasher44.uuid.uuid4
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.core.data.http.RemoteDataSource
import com.free.tvtracker.core.data.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

data class ShowsDataStatus(
    val data: TrackedShowApiResponse,
    /**
     * true if this data is sourced from the exact endpoint, false if this data is combined from other APIs
     * used to check in UI if this data includes the real endpoint's data.
     * Why not just have null data? So we can combine all the data on the repo layer and have 1 single source of shows
     */
    val fetched: Boolean
) {
    /**
     * this makes the stateflow emit the same value more than once
     */
    override fun equals(other: Any?): Boolean {
        return false
    }
}

class TrackedShowsRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalSqlDataProvider,
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue
) {
    private val allShows = MutableStateFlow<List<TrackedShowApiModel>>(emptyList())

    // these also contain all other shows, so they still must be filtered using the usecases before consumption
    private val _watchingShows = MutableStateFlow<ShowsDataStatus?>(null)
    private val _finishedShows = MutableStateFlow<ShowsDataStatus?>(null)
    private val _watchlistedShows = MutableStateFlow<ShowsDataStatus?>(null)
    val watchingShows: Flow<ShowsDataStatus> = _watchingShows.combineWithAllShows()
    val finishedShows: Flow<ShowsDataStatus> = _finishedShows.combineWithAllShows()
    val watchlistedShows: Flow<ShowsDataStatus> = _watchlistedShows.combineWithAllShows()

    private fun MutableStateFlow<ShowsDataStatus?>.combineWithAllShows(): Flow<ShowsDataStatus> {
        return this.filterNotNull()
            .combine(allShows) { watching, all ->
                watching.copy(
                    data = watching.data.copy(watching.data.data?.plus(all)?.distinctBy { it.id })
                )
            }
            .onEach { response ->
                allShows.emit(allShows.value.plus(response.data.data ?: emptyList()).distinctBy { it.id })
            }
    }

    suspend fun emitLatestWatching() {
        watchedEpisodesTaskQueue.emitOrders()
        val localWatching = localDataSource.getTrackedShows().map { it.toApiModel() }
        if (localWatching.isNotEmpty()) {
            _watchingShows.tryEmit(ShowsDataStatus(TrackedShowApiResponse.ok(localWatching), fetched = false))
        }
        fetch(_watchingShows, remoteDataSource::getTrackedShows)
    }

    suspend fun emitLatestFinished() {
        fetch(_finishedShows, remoteDataSource::getFinishedShows)
    }

    suspend fun emitLatestWatchlisted() {
        fetch(_watchlistedShows, remoteDataSource::getWatchlistedShows)
    }

    private suspend fun fetch(flow: MutableStateFlow<ShowsDataStatus?>, call: suspend () -> TrackedShowApiResponse) {
        val res = try {
            val res = call()
            res.asSuccess { data ->
                localDataSource.saveTrackedShows(data.map { it.toClientEntity() })
            }
            res
        } catch (e: Exception) {
            TrackedShowApiResponse.error(ApiError.Network)
        }
        if (res.isSuccess() || flow.value?.data?.isSuccess() != true) {
            flow.tryEmit(ShowsDataStatus(res, fetched = true))
        }
    }

    suspend fun getOrUpdateWatchingShows(): TrackedShowApiResponse {
        if (_watchingShows.value?.data != null) {
            return _watchingShows.value!!.data
        }
        emitLatestWatching()
        return _watchingShows.value!!.data
    }

    suspend fun markEpisodeAsWatched(episodes: List<MarkEpisodeWatched>) {
        val orders = episodes.map {
            MarkEpisodeWatchedOrderClientEntity(uuid4().toString(), it.trackedShowId.toLong(), it.episodeId.toLong())
        }
        if (orders.isEmpty()) return
        watchedEpisodesTaskQueue.add(orders)
    }

    suspend fun toggleWatchlist(showId: Int): Boolean {
        return remoteDataSource.toggleWatchlist(showId)
    }

    suspend fun addTrackedShow(showId: Int) {
        try {
            val res = remoteDataSource.addTracked(AddShowRequest(showId, false))
            res.coAsSuccess { newShow ->
                allShows.emit(allShows.value.plus(newShow))
            }
        } catch (e: Exception) {

        }
    }

    fun getShowByTmdbId(tmdbShowId: Int): TrackedShowApiModel? {
        return allShows.value.firstOrNull { it.storedShow.tmdbId == tmdbShowId }
    }

    fun getShowByTmdbIdFlow(tmdbShowId: Int): Flow<TrackedShowApiModel> {
        return allShows.map { it.firstOrNull { it.storedShow.tmdbId == tmdbShowId } }.filterNotNull()
    }
}

data class MarkEpisodeWatched(val trackedShowId: Int, val episodeId: Int)
