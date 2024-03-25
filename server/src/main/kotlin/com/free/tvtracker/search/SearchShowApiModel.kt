package com.free.tvtracker.search

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse

data class SearchShowApiModel(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("original_language") val originalLanguage: String? = null,
    @JsonProperty("original_name") val originalName: String? = null,
    @JsonProperty("overview") val overview: String,
    @JsonProperty("poster_path") val posterPath: String,
    @JsonProperty("genre_ids") val genreIds: List<Int>?,
    @JsonProperty("popularity") val popularity: Double? = null,
    @JsonProperty("first_air_date") val firstAirDate: String? = null,
    @JsonProperty("vote_average") val voteAverage: Double? = null,
    @JsonProperty("vote_count") val voteCount: Int? = null,
    @JsonProperty("origin_country") val originCountry: ArrayList<String> = arrayListOf()
)

fun TmdbSearchMultiResponse.Data.toShowApiModel(): SearchShowApiModel {
    return SearchShowApiModel(
        id = this.id!!,
        name = this.name!!,
        originalLanguage = this.originalLanguage,
        originalName = this.originalName,
        overview = this.overview!!,
        posterPath = this.posterPath!!,
        genreIds = this.genreIds,
        popularity = this.popularity,
        firstAirDate = this.firstAirDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        originCountry = this.originCountry,
    )
}
