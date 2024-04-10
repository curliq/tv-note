package com.free.tvtracker.tracked.api

import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.tracked.data.TrackedShowEntity
import com.free.tvtracker.tracked.data.TrackedShowEpisodeEntity

fun TrackedShowEntity.toApiModel(): TrackedShowApiModel {
    return TrackedShowApiModel(
        id = this.id,
        createdAtDatetime = this.createdAtDatetime,
        watchedEpisodes = this.watchedEpisodes.map { it.toApiModel() },
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
                posterImage = posterImage,
                status = status,
            )
        },
        watchlisted = this.watchlisted
    )
}

fun TrackedShowEpisodeEntity.toApiModel(): TrackedShowApiModel.WatchedEpisodeApiModel {
    return TrackedShowApiModel.WatchedEpisodeApiModel(
        id,
        storedEpisodeId
    )
}
