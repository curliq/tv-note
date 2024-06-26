package com.free.tvtracker.domain

import com.free.tvtracker.base.Reducer
import com.free.tvtracker.data.tracked.entities.MarkEpisodeWatchedOrderClientEntity
import com.free.tvtracker.tracked.response.TrackedContentApiModel

class TrackedShowReducer : Reducer<TrackedContentApiModel, List<MarkEpisodeWatchedOrderClientEntity>> {
    override fun reduce(
        from: TrackedContentApiModel,
        and: List<MarkEpisodeWatchedOrderClientEntity>
    ): TrackedContentApiModel {
        var eps = from.tvShow!!.watchedEpisodes
        and.forEach { order ->
            if (from.tvShow!!.id == order.showId.toInt()) {
                eps = eps.plus(
                    TrackedContentApiModel.TvShow.WatchedEpisodeApiModel(
                        id = "",
                        storedEpisodeId = order.episodeId.toInt(),
                    )
                )
            }
        }
        return from.copy(
            tvShow = from.tvShow!!.copy(
                watchedEpisodes = eps,
                storedShow = from.tvShow!!.storedShow.copy(
                    storedEpisodes = from.tvShow!!.storedShow.storedEpisodes.sortedWith(
                        compareBy({ it.season }, { it.episode })
                    )
                )
            )
        )
    }

}
