package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.search.response.SmallMovieApiModel

data class TmdbMovieSmallResponse(
    @JsonProperty("backdrop_path") val backdropPath: String? = null,
    @JsonProperty("id") val id: Int? = null,
    @JsonProperty("title") val title: String? = null,
    @JsonProperty("original_title") val originalTitle: String? = null,
    @JsonProperty("overview") val overview: String? = null,
    @JsonProperty("poster_path") val posterPath: String? = null,
    @JsonProperty("media_type") val mediaType: String? = null,
    @JsonProperty("adult") val adult: Boolean? = null,
    @JsonProperty("original_language") val originalLanguage: String? = null,
    @JsonProperty("genre_ids") val genreIds: List<Int>? = null,
    @JsonProperty("popularity") val popularity: Double? = null,
    @JsonProperty("release_date") val releaseDate: String? = null,
    @JsonProperty("video") val video: Boolean? = null,
    @JsonProperty("vote_average") val voteAverage: Double? = null,
    @JsonProperty("vote_count") val voteCount: Int? = null
) {
    fun toApiModel(): SmallMovieApiModel {
        return SmallMovieApiModel(
            backdropPath = backdropPath,
            tmdbId = id!!,
            title = title!!,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            posterPath = posterPath,
            popularity = popularity,
            releaseDate = releaseDate,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount,
        )
    }
}
