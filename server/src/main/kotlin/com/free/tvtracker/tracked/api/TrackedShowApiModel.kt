package com.free.tvtracker.tracked.api

import com.free.besttvtracker.TrackedShowApiModel
import com.free.tvtracker.tracked.data.TrackedShowEntity

fun TrackedShowEntity.toApiModel(): TrackedShowApiModel {
    return TrackedShowApiModel(
        id = this.id,
        createdAtDatetime = this.createdAtDatetime,
        watchedEpisodeIds = this.watchedEpisodes.map { it.id },
        storedShow = this.storedShow.run {
            TrackedShowApiModel.StoredShowApiModel(
                tmdbId = tmdbId,
                title = title,
                storedEpisodes = storedEpisodes.map { ep ->
                    TrackedShowApiModel.StoredEpisodeApiModel(
                        id = ep.id.toString(),
                        season = ep.seasonNumber,
                        episode = ep.episodeNumber
                    )
                },
                posterImage = posterImage
            )
        },
        watchlisted = this.watchlisted
    )
}
