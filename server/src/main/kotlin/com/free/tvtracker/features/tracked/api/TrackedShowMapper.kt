package com.free.tvtracker.features.tracked.api

import com.free.tvtracker.features.tracked.data.movies.TrackedMovieEntity
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEntity
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEpisodeEntity
import com.free.tvtracker.features.watchlists.data.WatchlistTrackedShowEntity
import com.free.tvtracker.storage.shows.data.StoredEpisodeEntity
import com.free.tvtracker.tracked.response.TrackedContentApiModel

/**
 * the lists are passed here because accessing it through hibernate throws concurrent exception
 */
fun TrackedShowEntity.toApiModel(
    storedEpisodes: List<StoredEpisodeEntity>? = null,
    watchlists: List<WatchlistTrackedShowEntity>? = null,
    watchedEpisodes: List<TrackedShowEpisodeEntity>? = null,
): TrackedContentApiModel {
    val watchedEpisodes = watchedEpisodes ?: this.watchedEpisodes.toList()
    val storedEpisodes = storedEpisodes ?: this.storedShow.storedEpisodes.toList()
    val watchlistTrackedShows = watchlists ?: this.watchlistTrackedShows.toList()
    return TrackedContentApiModel(
        watchlisted = this.watchlisted,
        tvShow = TrackedContentApiModel.TvShow(
            id = this.id,
            createdAtDatetime = this.createdAtDatetime,
            watchedEpisodes = watchedEpisodes.map {
                it.toApiModel()
            },
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
        mediaType = TrackedContentApiModel.ContentType.TvShow,
        watchlists = watchlistTrackedShows.map {
            TrackedContentApiModel.Watchlist(it.watchlist.id, it.watchlist.name)
        }
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
        mediaType = TrackedContentApiModel.ContentType.Movie,
        watchlists = this.watchlistTrackedMovies.map {
            TrackedContentApiModel.Watchlist(it.watchlist.id, it.watchlist.name)
        }
    )
}

fun TrackedShowEpisodeEntity.toApiModel(): TrackedContentApiModel.TvShow.WatchedEpisodeApiModel {
    return TrackedContentApiModel.TvShow.WatchedEpisodeApiModel(
        id,
        storedEpisodeId
    )
}
