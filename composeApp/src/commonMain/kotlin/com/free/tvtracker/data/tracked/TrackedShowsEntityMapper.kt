package com.free.tvtracker.data.tracked

import com.free.tvtracker.data.tracked.entities.StoredEpisodeClientEntity
import com.free.tvtracker.data.tracked.entities.StoredShowClientEntity
import com.free.tvtracker.data.tracked.entities.TrackedShowClientEntity
import com.free.tvtracker.data.tracked.entities.WatchedEpisodeClientEntity
import com.free.tvtracker.tracked.response.TrackedContentApiModel

fun TrackedContentApiModel.toClientEntity(): TrackedShowClientEntity {
    return TrackedShowClientEntity(
        this.tvShow!!.id.toLong(),
        this.tvShow!!.createdAtDatetime,
        this.tvShow!!.watchedEpisodes.map { it.toClientEntity(this.tvShow!!.id.toLong()) },
        this.tvShow!!.storedShow.toClientEntity(),
        this.watchlisted
    )
}

fun TrackedShowClientEntity.toApiModel(): TrackedContentApiModel {
    return TrackedContentApiModel(
        this.watchlisted,
        TrackedContentApiModel.ContentType.TvShow,
        tvShow = TrackedContentApiModel.TvShow(
            this.id.toInt(),
            this.createdAtDatetime,
            this.watchedEpisodes.map { it.toApiModel() },
            this.storedShow.toApiModel(),
        ),
        movie = null,
        watchlists = emptyList(), // watchlists not needed in cache (test)
    )
}

fun TrackedContentApiModel.TvShow.StoredShowApiModel.toClientEntity(): StoredShowClientEntity {
    return StoredShowClientEntity(
        this.tmdbId.toLong(),
        this.title,
        this.storedEpisodes.map { it.toClientEntity() },
        this.posterImage,
        this.backdropImage,
        this.status
    )
}

fun StoredShowClientEntity.toApiModel(): TrackedContentApiModel.TvShow.StoredShowApiModel {
    return TrackedContentApiModel.TvShow.StoredShowApiModel(
        this.tmdbId.toInt(),
        this.title,
        this.storedEpisodes.map { it.toApiModel() },
        this.posterImage,
        this.backdropImage,
        this.status,
    )
}

fun TrackedContentApiModel.TvShow.StoredEpisodeApiModel.toClientEntity(): StoredEpisodeClientEntity {
    return StoredEpisodeClientEntity(
        this.id.toLong(),
        this.season.toLong(),
        this.episode.toLong(),
        this.airDate
    )
}

fun StoredEpisodeClientEntity.toApiModel(): TrackedContentApiModel.TvShow.StoredEpisodeApiModel {
    return TrackedContentApiModel.TvShow.StoredEpisodeApiModel(
        this.id.toInt(),
        this.season.toInt(),
        this.episode.toInt(),
        this.airDate
    )
}

fun TrackedContentApiModel.TvShow.WatchedEpisodeApiModel.toClientEntity(trackedShowId: Long): WatchedEpisodeClientEntity {
    return WatchedEpisodeClientEntity(
        this.id,
        this.storedEpisodeId.toLong(),
        trackedShowId = trackedShowId
    )
}

fun WatchedEpisodeClientEntity.toApiModel(): TrackedContentApiModel.TvShow.WatchedEpisodeApiModel {
    return TrackedContentApiModel.TvShow.WatchedEpisodeApiModel(
        this.id,
        this.storedEpisodeId.toInt()
    )
}
