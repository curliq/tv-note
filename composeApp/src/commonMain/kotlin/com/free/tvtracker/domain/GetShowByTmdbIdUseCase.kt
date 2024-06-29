package com.free.tvtracker.domain

import com.free.tvtracker.data.search.SearchRepository
import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.details.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetShowByTmdbIdUseCase(
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val searchRepository: SearchRepository,
    private val trackedShowReducer: TrackedShowReducer,
    private val locationService: CachingLocationService,
) {
    data class GetTrackedShowByTmdbIdResult(
        val showData: TmdbShowDetailsApiResponse,
        val tracked: TrackedContentApiModel?
    )

    operator fun invoke(tmdbShowId: Int): Flow<GetTrackedShowByTmdbIdResult> {
        val countryCode = locationService.getCountryCode()
        val showDataResponse = flow {
            val res = searchRepository.getShow(tmdbShowId, includeEpisodes = true, countryCode = countryCode)
            emit(res)
        }
        val trackedShow = trackedShowsRepository.getByTmdbIdFlow(tmdbShowId)
        val watchedEpisodes = watchedEpisodesTaskQueue.watchedEpisodeOrders
        return combine(showDataResponse, trackedShow, watchedEpisodes, transform = { a, b, c ->
            GetTrackedShowByTmdbIdResult(a, if (b == null) null else trackedShowReducer.reduce(b, c))
        })
    }
}
