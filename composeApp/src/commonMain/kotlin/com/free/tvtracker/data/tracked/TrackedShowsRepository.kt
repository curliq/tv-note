package com.free.tvtracker.data.tracked

import com.benasher44.uuid.uuid4
import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    private val httpClient: TvHttpClientEndpoints,
    private val localDataSource: LocalSqlDataProvider,
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue
) {
    val allShows = MutableStateFlow<List<TrackedShowApiModel>>(emptyList())

    // these also contain all other shows, so they still must be filtered using the usecases before consumption
    private val _watchingShows = MutableStateFlow<ShowsDataStatus?>(null)
    private val _finishedShows = MutableStateFlow<ShowsDataStatus?>(null)
    private val _watchlistedShows = MutableStateFlow<ShowsDataStatus?>(null)
    val watchingShows: Flow<ShowsDataStatus> = _watchingShows.combineWithAllShows()
    val finishedShows: Flow<ShowsDataStatus> = _finishedShows.combineWithAllShows()
    val watchlistedShows: Flow<ShowsDataStatus> = _watchlistedShows.combineWithAllShows()

    private fun MutableStateFlow<ShowsDataStatus?>.combineWithAllShows(): Flow<ShowsDataStatus> {
        return this.filterNotNull()
            .onEach { response ->
                allShows.emit(allShows.value.plus(response.data.data ?: emptyList()).distinctBy { it.id })
            }
            .combine(allShows) { watching, all ->
                val asd = all.filterNot { watching.data.data?.map { it.id }?.contains(it.id) == true }
                watching.copy(
                    data = watching.data.copy(watching.data.data?.plus(asd))
                )
            }
    }

    suspend fun updateWatching(forceUpdate: Boolean = true) {
        if (!forceUpdate && _watchingShows.value != null) {
            return
        }
        watchedEpisodesTaskQueue.emitOrders()
        val localWatching = localDataSource.getTrackedShows().map { it.toApiModel() }
        if (localWatching.isNotEmpty()) {
            _watchingShows.tryEmit(ShowsDataStatus(TrackedShowApiResponse.ok(localWatching), fetched = false))
        }
        fetch(_watchingShows, ::getTrackedShows)
    }

    suspend fun updateFinished(forceUpdate: Boolean = true) {
        if (!forceUpdate && _finishedShows.value != null) {
            return
        }
        fetch(_finishedShows, ::getFinishedShows)
    }

    suspend fun updateWatchlisted(forceUpdate: Boolean = true) {
        if (!forceUpdate && _watchlistedShows.value != null) {
            return
        }
        fetch(_watchlistedShows, ::getWatchlistedShows)
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

    fun getWatchingShows(): List<TrackedShowApiModel> {
        return allShows.value
    }

    suspend fun markEpisodeAsWatched(episodes: List<MarkEpisodeWatched>) {
        val orders = episodes.map {
            MarkEpisodeWatchedOrderClientEntity(uuid4().toString(), it.trackedShowId.toLong(), it.episodeId.toLong())
        }
        if (orders.isEmpty()) return
        watchedEpisodesTaskQueue.add(orders)
    }

    suspend fun toggleWatchlist(showId: Int): Boolean {
        return toggleWatchlist(showId)
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun addTrackedShow(showId: Int, watchlisted: Boolean = false) {
        // Use global scope because this should finish even if the user closes the search activity
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val res = addTracked(AddShowApiRequestBody(showId, watchlisted = watchlisted))
                res.coAsSuccess { newShow ->
                    allShows.update { it.plus(newShow) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getShowByTmdbId(tmdbShowId: Int): TrackedShowApiModel? {
        return allShows.value.firstOrNull { it.storedShow.tmdbId == tmdbShowId }
    }

    fun getShowByTmdbIdFlow(tmdbShowId: Int): Flow<TrackedShowApiModel?> {
        return allShows.map { it.firstOrNull { it.storedShow.tmdbId == tmdbShowId } }
    }

    private suspend fun getTrackedShows(): TrackedShowApiResponse {
        return httpClient.getWatching()
    }

    private suspend fun getFinishedShows(): TrackedShowApiResponse {
        return httpClient.getFinished()
    }

    private suspend fun getWatchlistedShows(): TrackedShowApiResponse {
        return httpClient.getWatchlisted()
    }

    private suspend fun addTracked(body: AddShowApiRequestBody): AddTrackedShowApiResponse {
        return httpClient.call(Endpoints.addTracked, body)
    }
}

data class MarkEpisodeWatched(val trackedShowId: Int, val episodeId: Int)
