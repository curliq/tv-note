package com.free.tvtracker.domain

import com.free.tvtracker.data.tracked.ShowsData
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

open class GetShowsUseCase(
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
    private val trackedShowReducer: TrackedShowReducer
) {
    operator fun invoke(flow: Flow<ShowsData>): Flow<ShowsData> =
        flow.combine(watchedEpisodesTaskQueue.watchedEpisodeOrders) { shows, orders ->
            shows.copy(data = shows.data.map { show ->
                if (show.isTvShow) {
                    trackedShowReducer.reduce(show, orders)
                } else {
                    show
                }
            })
        }
}
