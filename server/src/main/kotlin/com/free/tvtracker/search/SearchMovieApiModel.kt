package com.free.tvtracker.search

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse

data class SearchMovieApiModel(
    @JsonProperty("backdrop_path") val backdropPath: String? = null,
    @JsonProperty("id") val id: Int,
    @JsonProperty("title") val title: String,
    @JsonProperty("original_language") val originalLanguage: String? = null,
    @JsonProperty("original_title") val originalTitle: String? = null,
    @JsonProperty("overview") val overview: String,
    @JsonProperty("poster_path") val posterPath: String,
    @JsonProperty("genre_ids") val genreIds: List<Int>? = null,
    @JsonProperty("popularity") val popularity: Double? = null,
    @JsonProperty("release_date") val releaseDate: String? = null,
    @JsonProperty("video") val video: Boolean? = null,
    @JsonProperty("vote_average") val voteAverage: Double? = null,
    @JsonProperty("vote_count") val voteCount: Int? = null,
)

fun TmdbSearchMultiResponse.Data.toMovieApiModel(): SearchMovieApiModel {
    return SearchMovieApiModel(
        backdropPath = backdropPath,
        id = id!!,
        title = title!!,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview!!,
        posterPath = posterPath!!,
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )
}
