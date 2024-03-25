package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

class TmdbSearchMultiResponse : TmdbListResponse<TmdbSearchMultiResponse.Data>() {
    data class Data(
        @JsonProperty("adult") val adult: Boolean? = null,
        @JsonProperty("backdrop_path") val backdropPath: String? = null,
        @JsonProperty("id") val id: Int? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("original_language") val originalLanguage: String? = null,
        @JsonProperty("original_title") val originalTitle: String? = null,
        @JsonProperty("overview") val overview: String? = null,
        @JsonProperty("poster_path") val posterPath: String? = null,
        @JsonProperty("media_type") val mediaType: String? = null,
        @JsonProperty("genre_ids") val genreIds: List<Int>? = null,
        @JsonProperty("popularity") val popularity: Double? = null,
        @JsonProperty("release_date") val releaseDate: String? = null,
        @JsonProperty("video") val video: Boolean? = null,
        @JsonProperty("vote_average") val voteAverage: Double? = null,
        @JsonProperty("vote_count") val voteCount: Int? = null,
        @JsonProperty("name") val name: String? = null,
        @JsonProperty("original_name") val originalName: String? = null,
        @JsonProperty("first_air_date") val firstAirDate: String? = null,
        @JsonProperty("profile_path") val profilePath: String? = null,
        @JsonProperty("origin_country") val originCountry: ArrayList<String> = arrayListOf(),
        @JsonProperty("gender") val gender: Int? = null,
        @JsonProperty("known_for_department") val knownForDepartment: String? = null,
        @JsonProperty("known_for") val knownFor: List<Data>? = null,
    )
}
