package com.free.tvtracker.domain

import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetTrackedShowByTmdbIdUseCase(
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val searchRepository: SearchRepository,
    private val trackedShowReducer: TrackedShowReducer,
    private val locationService: CachingLocationService,
) {
    data class GetTrackedShowByTmdbIdResult(val showData: TmdbShowDetailsApiResponse, val tracked: TrackedShowApiModel?)

    operator fun invoke(tmdbShowId: Int): Flow<GetTrackedShowByTmdbIdResult> {
        val showDataResponse = flow {
            val countryCode = locationService.getCountryCode()
            val res = searchRepository.getShow(tmdbShowId, includeEpisodes = true, countryCode = countryCode)
            emit(res)
        }
        val trackedShow = trackedShowsRepository.getShowByTmdbIdFlow(tmdbShowId)
        val watchedEpisodes = watchedEpisodesTaskQueue.watchedEpisodeOrders
        return combine(showDataResponse, trackedShow, watchedEpisodes, transform = { a, b, c ->
            GetTrackedShowByTmdbIdResult(a, if (b == null) null else trackedShowReducer.reduce(b, c))
        }).catch { it.printStackTrace() }
    }
}
