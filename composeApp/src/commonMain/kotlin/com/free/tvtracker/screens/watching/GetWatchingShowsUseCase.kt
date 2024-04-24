package com.free.tvtracker.screens.watching

import com.free.tvtracker.data.tracked.TrackedShowsRepository
import com.free.tvtracker.data.tracked.WatchedEpisodesTaskQueue
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetWatchingShowsUseCase(
    private val repository: TrackedShowsRepository,
    private val watchedEpisodesTaskQueue: WatchedEpisodesTaskQueue,
) {
    operator fun invoke(): Flow<TrackedShowApiResponse> =
        repository.watchingShows.combine(watchedEpisodesTaskQueue.watchedEpisodeOrders) { shows, orders ->
            shows.copy(data = shows.data?.map { show ->
                var eps = show.watchedEpisodes
                orders.forEach { order ->
                    if (show.id == order.showId.toInt()) {
                        eps = eps.plus(
                            TrackedShowApiModel.WatchedEpisodeApiModel(
                                id = -1,
                                storedEpisodeId = order.episodeId,
                            )
                        )
                    }
                }
                show.copy(
                    watchedEpisodes = eps
                )
            })
        }
}
