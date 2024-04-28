package com.free.tvtracker.domain

import com.free.tvtracker.base.Reducer
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.tracked.response.TrackedShowApiModel


class TrackedShowReducer : Reducer<TrackedShowApiModel, List<MarkEpisodeWatchedOrderClientEntity>> {
    override fun reduce(
        from: TrackedShowApiModel,
        and: List<MarkEpisodeWatchedOrderClientEntity>
    ): TrackedShowApiModel {
        var eps = from.watchedEpisodes
        and.forEach { order ->
            if (from.id == order.showId.toInt()) {
                eps = eps.plus(
                    TrackedShowApiModel.WatchedEpisodeApiModel(
                        id = "",
                        storedEpisodeId = order.episodeId.toInt(),
                    )
                )
            }
        }
        return from.copy(
            watchedEpisodes = eps,
            storedShow = from.storedShow.copy(
                storedEpisodes = from.storedShow.storedEpisodes.sortedWith(
                    compareBy({ it.season }, { it.episode })
                )
            )
        )
    }

}
