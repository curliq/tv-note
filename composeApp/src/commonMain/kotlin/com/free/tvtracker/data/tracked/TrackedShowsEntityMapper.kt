package com.free.tvtracker.data.tracked

import com.free.tvtracker.data.tracked.entities.StoredEpisodeClientEntity
import com.free.tvtracker.data.tracked.entities.StoredShowClientEntity
import com.free.tvtracker.data.tracked.entities.TrackedShowClientEntity
import com.free.tvtracker.data.tracked.entities.WatchedEpisodeClientEntity
import com.free.tvtracker.tracked.response.TrackedShowApiModel

fun TrackedShowApiModel.toClientEntity(): TrackedShowClientEntity {
    return TrackedShowClientEntity(
        this.id.toLong(),
        this.createdAtDatetime,
        this.watchedEpisodes.map { it.toClientEntity(this.id.toLong()) },
        this.storedShow.toClientEntity(),
        this.watchlisted
    )
}

fun TrackedShowClientEntity.toApiModel(): TrackedShowApiModel {
    return TrackedShowApiModel(
        this.id.toInt(),
        this.createdAtDatetime,
        this.watchedEpisodes.map { it.toApiModel() },
        this.storedShow.toApiModel(),
        this.watchlisted
    )
}

fun TrackedShowApiModel.StoredShowApiModel.toClientEntity(): StoredShowClientEntity {
    return StoredShowClientEntity(
        this.tmdbId.toLong(),
        this.title,
        this.storedEpisodes.map { it.toClientEntity() },
        this.posterImage,
        this.backdropImage,
        this.status
    )
}

fun StoredShowClientEntity.toApiModel(): TrackedShowApiModel.StoredShowApiModel {
    return TrackedShowApiModel.StoredShowApiModel(
        this.tmdbId.toInt(),
        this.title,
        this.storedEpisodes.map { it.toApiModel() },
        this.posterImage,
        this.backdropImage,
        this.status,
    )
}

fun TrackedShowApiModel.StoredEpisodeApiModel.toClientEntity(): StoredEpisodeClientEntity {
    return StoredEpisodeClientEntity(
        this.id.toLong(),
        this.season.toLong(),
        this.episode.toLong(),
        this.airDate
    )
}

fun StoredEpisodeClientEntity.toApiModel(): TrackedShowApiModel.StoredEpisodeApiModel {
    return TrackedShowApiModel.StoredEpisodeApiModel(
        this.id.toInt(),
        this.season.toInt(),
        this.episode.toInt(),
        this.airDate
    )
}

fun TrackedShowApiModel.WatchedEpisodeApiModel.toClientEntity(trackedShowId: Long): WatchedEpisodeClientEntity {
    return WatchedEpisodeClientEntity(
        this.id,
        this.storedEpisodeId.toLong(),
        trackedShowId = trackedShowId
    )
}

fun WatchedEpisodeClientEntity.toApiModel(): TrackedShowApiModel.WatchedEpisodeApiModel {
    return TrackedShowApiModel.WatchedEpisodeApiModel(
        this.id,
        this.storedEpisodeId.toInt()
    )
}
