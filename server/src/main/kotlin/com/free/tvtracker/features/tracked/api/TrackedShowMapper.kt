package com.free.tvtracker.features.tracked.api

import com.free.tvtracker.features.tracked.data.movies.TrackedMovieEntity
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEntity
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEpisodeEntity
import com.free.tvtracker.tracked.response.TrackedContentApiModel

fun TrackedShowEntity.toApiModel(): TrackedContentApiModel {
    return TrackedContentApiModel(
        watchlisted = this.watchlisted,
        tvShow = TrackedContentApiModel.TvShow(
            id = this.id,
            createdAtDatetime = this.createdAtDatetime,
            watchedEpisodes = this.watchedEpisodes.map { it.toApiModel() },
            storedShow = this.storedShow.run {
                TrackedContentApiModel.TvShow.StoredShowApiModel(
                    tmdbId = tmdbId,
                    title = title,
                    storedEpisodes = storedEpisodes.map { ep ->
                        TrackedContentApiModel.TvShow.StoredEpisodeApiModel(
                            id = ep.id,
                            season = ep.seasonNumber,
                            episode = ep.episodeNumber,
                            airDate = ep.airDate
                        )
                    },
                    posterImage = posterImage,
                    backdropImage = backdropImage,
                    status = status,
                )
            }
        ),
        movie = null,
        mediaType = TrackedContentApiModel.ContentType.TvShow
    )
}

fun TrackedMovieEntity.toApiModel(): TrackedContentApiModel {
    return TrackedContentApiModel(
        watchlisted = this.watchlisted,
        tvShow = null,
        movie = TrackedContentApiModel.Movie(
            id = this.id,
            createdAtDatetime = this.createdAtDatetime,
            storedMovie = TrackedContentApiModel.Movie.StoredMovieApiModel(
                tmdbId = this.storedMovie.tmdbId,
                title = this.storedMovie.title,
                posterImage = this.storedMovie.posterImage,
                backdropImage = this.storedMovie.backdropImage,
                releaseDate = this.storedMovie.releaseDate
            )
        ),
        mediaType = TrackedContentApiModel.ContentType.Movie
    )
}

fun TrackedShowEpisodeEntity.toApiModel(): TrackedContentApiModel.TvShow.WatchedEpisodeApiModel {
    return TrackedContentApiModel.TvShow.WatchedEpisodeApiModel(
        id,
        storedEpisodeId
    )
}
