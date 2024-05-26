package com.free.tvtracker.features.tracked.api

import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.features.tracked.data.TrackedShowEntity
import com.free.tvtracker.features.tracked.data.TrackedShowEpisodeEntity

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
                        id = ep.id,
                        season = ep.seasonNumber,
                        episode = ep.episodeNumber,
                        airDate = ep.airDate
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