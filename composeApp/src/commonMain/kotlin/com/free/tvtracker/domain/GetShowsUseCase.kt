package com.free.tvtracker.domain

import com.free.tvtracker.data.tracked.ShowsDataStatus
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

open class GetShowsUseCase(
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val trackedShowReducer: TrackedShowReducer
) {
    operator fun invoke(flow: Flow<ShowsDataStatus>): Flow<ShowsDataStatus> =
        flow.combine(watchedEpisodesTaskQueue.watchedEpisodeOrders) { shows, orders ->
            shows.copy(data = shows.data.copy(shows.data.data?.map { show ->
                trackedShowReducer.reduce(show, orders)
            }))
        }
}
