package com.free.tvtracker.tracked.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackedContentApiModel(
    @SerialName("watchlisted") val watchlisted: Boolean,
    @SerialName("content_type") val mediaType: ContentType,
    @SerialName("tv_show") val tvShow: TvShow?,
    @SerialName("movie") val movie: Movie?,
    @SerialName("lists") var watchlists: List<Watchlist>
) {
    @Serializable
    enum class ContentType(val key: String) {
        @SerialName("tv_show")
        TvShow("tv_show"),

        @SerialName("person")
        Person("person"),

        @SerialName("movie")
        Movie("movie");

        override fun toString(): String {
            return key
        }
    }

    val typedId: String
        get() {
            return buildPseudoId(mediaType, tvShow?.storedShow?.tmdbId ?: movie!!.storedMovie.tmdbId)
        }

    val isTvShow: Boolean
        get() = mediaType == ContentType.TvShow

    val anyTmdbId: Int
        get() = if (isTvShow) tvShow!!.storedShow.tmdbId else movie!!.storedMovie.tmdbId

    @Serializable
    data class Movie(
        @SerialName("id") val id: Int,
        @SerialName("created_at_datetime") val createdAtDatetime: String,
        @SerialName("stored_movie") val storedMovie: StoredMovieApiModel,
    ) {
        @Serializable
        data class StoredMovieApiModel(
            @SerialName("tmdb_id") val tmdbId: Int,
            @SerialName("title") val title: String,
            @SerialName("poster_image") val posterImage: String?,
            @SerialName("backdrop_image") val backdropImage: String?,
            @SerialName("release_date") val releaseDate: String?,
        )
    }

    @Serializable
    data class TvShow(
        @SerialName("id") val id: Int,
        @SerialName("created_at_datetime") val createdAtDatetime: String,
        @SerialName("watched_episodes") val watchedEpisodes: List<WatchedEpisodeApiModel>,
        @SerialName("stored_show") val storedShow: StoredShowApiModel,
    ) {
        @Serializable
        data class WatchedEpisodeApiModel(
            @SerialName("id") val id: String,
            @SerialName("stored_episode_id") val storedEpisodeId: Int,
        )

        @Serializable
        data class StoredShowApiModel(
            @SerialName("tmdb_id") val tmdbId: Int,
            @SerialName("title") val title: String,
            @SerialName("stored_episodes") val storedEpisodes: List<StoredEpisodeApiModel>,
            @SerialName("poster_image") val posterImage: String?,
            @SerialName("backdrop_image") val backdropImage: String?,
            @SerialName("status") val status: String,
        )

        @Serializable
        data class StoredEpisodeApiModel(
            @SerialName("id") val id: Int,
            @SerialName("season") val season: Int,
            @SerialName("episode") val episode: Int,
            @SerialName("air_date") val airDate: String?,
        )
    }

    @Serializable
    data class Watchlist(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String
    )
}
