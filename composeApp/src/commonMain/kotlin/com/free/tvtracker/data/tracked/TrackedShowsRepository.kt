package com.free.tvtracker.data.tracked

import com.benasher44.uuid.uuid4
import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.tracked.request.AddMovieApiRequestBody
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ShowsData(
    val data: List<TrackedContentApiModel>,
    val status: ShowsDataStatus
)

data class ShowsDataStatus(
    /**
     * true if this data is sourced from the exact endpoint, false if this data is combined from other APIs
     * used to check in UI if this data includes the real endpoint's data.
     * Why not just have null data? So we can combine all the data on the repo layer and have 1 single source of shows
     */
    val fetched: Boolean?,
    val success: Boolean
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
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val logger: Logger,
) {
    val allShows = MutableStateFlow<List<TrackedContentApiModel>>(emptyList())

    private val _watchingShows = MutableStateFlow(ShowsDataStatus(null, false))
    private val _finishedShows = MutableStateFlow(ShowsDataStatus(null, false))
    private val _watchlistedShows = MutableStateFlow(ShowsDataStatus(null, false))
    val watchingShows: Flow<ShowsData> = _watchingShows.combineWithAllShows()
    val finishedShows: Flow<ShowsData> = _finishedShows.combineWithAllShows()
    val watchlistedShows: Flow<ShowsData> = _watchlistedShows.combineWithAllShows()

    private fun MutableStateFlow<ShowsDataStatus>.combineWithAllShows(): Flow<ShowsData> {
        return this.combine(allShows) { watching, all ->
            logger.d("shows flow updated: ${all.map { it.typedId }}")
            ShowsData(status = watching, data = all)
        }
    }

    suspend fun updateWatching(forceUpdate: Boolean = true) {
        logger.d("updating watching shows")
        if (!forceUpdate && _watchingShows.value.fetched == true) {
            return
        }
        watchedEpisodesTaskQueue.emitOrders()
        val localWatching = localDataSource.getTrackedShows().map { it.toApiModel() }.filter { !it.watchlisted }
        if (localWatching.isNotEmpty()) {
            logger.d("local shows is empty")
            _watchingShows.emit(ShowsDataStatus(false, true))
            allShows.emit(allShows.value.plus(localWatching).distinctBy { it.typedId })
        }
        fetch(_watchingShows, ::getTrackedShows, ignoreResultIfError = true).coAsSuccess {
            logger.d("local shows response success")
            _watchingShows.emit(ShowsDataStatus(true, true))
            localDataSource.saveTrackedShows(it.map { it.toClientEntity() })
        }
    }

    suspend fun updateFinished(forceUpdate: Boolean = true) {
        logger.d("updating finished shows")
        if (!forceUpdate && _finishedShows.value.fetched == true) {
            return
        }
        fetch(_finishedShows, ::getFinishedShows)
    }

    suspend fun updateWatchlisted(forceUpdate: Boolean = true) {
        logger.d("updating watchlisted shows")
        if (!forceUpdate && _watchlistedShows.value.fetched == true) {
            return
        }
        fetch(_watchlistedShows, ::getWatchlistedShows)
    }

    private suspend fun fetch(
        flow: MutableStateFlow<ShowsDataStatus>,
        call: suspend () -> TrackedShowsApiResponse,
        ignoreResultIfError: Boolean = false
    ): TrackedShowsApiResponse {
        val res = try {
            call()
        } catch (e: Exception) {
            logger.e(e)
            TrackedShowsApiResponse.error(ApiError.Network)
        }
        if (res.isSuccess()) {
            allShows.update {
                it.plus(res.data!!).distinctBy { it.typedId }
            }
        }
        if (!ignoreResultIfError) {
            flow.emit(ShowsDataStatus(true, res.isSuccess()))
        }
        return res
    }

    suspend fun markEpisodeAsWatched(episodes: List<MarkEpisodeWatched>) {
        val orders = episodes.map {
            MarkEpisodeWatchedOrderClientEntity(uuid4().toString(), it.trackedShowId.toLong(), it.episodeId.toLong())
        }
        if (orders.isEmpty()) return
        watchedEpisodesTaskQueue.add(orders)
    }

    suspend fun setWatchlisted(trackedContentId: Int, isTvShow: Boolean, watchlisted: Boolean) {
        val res = httpClient.setWatchlisted(trackedContentId, isTvShow, watchlisted)
        res.asSuccess { show ->
            logger.d("set watchlisted: $trackedContentId")
            allShows.update {
                val oldShow = if (isTvShow) {
                    it.first { it.tvShow?.id == trackedContentId }
                } else {
                    it.first { it.movie?.id == trackedContentId }
                }
                it.minus(oldShow).plus(show)
            }
            localDataSource.saveTrackedShows(listOf(show.toClientEntity()))
        }
    }

    suspend fun removeContent(trackedContentId: Int, isTvShow: Boolean) {
        val res = httpClient.removeTrackedShow(trackedContentId, isTvShow)
        res.asSuccess {
            logger.d("remove content: $trackedContentId")
            if (isTvShow) {
                localDataSource.deleteTrackedShow(trackedContentId)
                localDataSource.deleteEpisodeWatchedOrderForTvShow(trackedContentId)
            }
            allShows.update {
                val oldShow = if (isTvShow) {
                    it.first { it.tvShow?.id == trackedContentId }
                } else {
                    it.first { it.movie?.id == trackedContentId }
                }
                it.minus(oldShow)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addTrackedShow(
        tmdbId: Int,
        isTvShow: Boolean,
        watchlisted: Boolean = false,
        finished: Boolean = false
    ) {
        // Use GlobalScope because this should finish even if the user closes the search activity
        GlobalScope.launch(Dispatchers.IO) {
            coAddTrackedShow(tmdbId, isTvShow, watchlisted, finished)
        }
    }

    suspend fun coAddTrackedShow(
        tmdbId: Int,
        isTvShow: Boolean,
        watchlisted: Boolean = false,
        finished: Boolean = false
    ): TrackedContentApiModel? {
        try {
            val res = if (isTvShow) {
                addTrackedShow(
                    AddShowApiRequestBody(
                        tmdbShowId = tmdbId,
                        watchlisted = watchlisted,
                        addAllEpisodes = finished
                    )
                )
            } else {
                addTrackedMovie(AddMovieApiRequestBody(tmdbId, watchlisted = watchlisted))
            }
            if (res.isSuccess()) {
                logger.d("add tracked: $tmdbId")
                allShows.update { it.plus(res.data!!) }
                localDataSource.saveTrackedShows(listOf(res.data!!.toClientEntity()))
                return res.data!!
            }
        } catch (e: Exception) {
            logger.e(e)
            return null
        }
        return null
    }

    fun getByTmdbId(tmdbShowId: Int): TrackedContentApiModel? {
        return allShows.value.firstOrNull { it.anyTmdbId == tmdbShowId }
    }

    fun getByTmdbIdFlow(tmdbShowId: Int): Flow<TrackedContentApiModel?> {
        return allShows.map { it.firstOrNull { it.anyTmdbId == tmdbShowId } }
    }

    private suspend fun getTrackedShows(): TrackedShowsApiResponse {
        return httpClient.getWatching()
    }

    private suspend fun getFinishedShows(): TrackedShowsApiResponse {
        return httpClient.getFinished()
    }

    private suspend fun getWatchlistedShows(): TrackedShowsApiResponse {
        return httpClient.getWatchlisted()
    }

    private suspend fun addTrackedShow(body: AddShowApiRequestBody): AddTrackedShowApiResponse {
        return httpClient.call(Endpoints.addTrackedShow, body)
    }

    private suspend fun addTrackedMovie(body: AddMovieApiRequestBody): AddTrackedShowApiResponse {
        return httpClient.call(Endpoints.addTrackedMovie, body)
    }
}

data class MarkEpisodeWatched(val trackedShowId: Int, val episodeId: Int)
