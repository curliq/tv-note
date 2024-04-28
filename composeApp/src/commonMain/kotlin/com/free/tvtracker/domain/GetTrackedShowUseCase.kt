package com.free.tvtracker.domain

import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTrackedShowUseCase(
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val trackedShowsRepository: TrackedShowsRepository,
    private val trackedShowReducer: TrackedShowReducer
) {
    operator fun invoke(tmdbShowId: Int): Flow<TrackedShowApiModel> =
        trackedShowsRepository.getShowByTmdbIdFlow(tmdbShowId)
            .combine(watchedEpisodesTaskQueue.watchedEpisodeOrders) { show, orders ->
                trackedShowReducer.reduce(show, orders)
            }
}
