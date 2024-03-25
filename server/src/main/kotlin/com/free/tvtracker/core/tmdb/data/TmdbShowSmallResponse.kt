package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbShowSmallResponse(
    @JsonProperty("adult") var adult: Boolean? = null,
    @JsonProperty("backdrop_path") var backdropPath: String? = null,
    @JsonProperty("id") var id: Int? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("original_language") var originalLanguage: String? = null,
    @JsonProperty("original_name") var originalName: String? = null,
    @JsonProperty("overview") var overview: String? = null,
    @JsonProperty("poster_path") var posterPath: String? = null,
    @JsonProperty("media_type") var mediaType: String? = null,
    @JsonProperty("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @JsonProperty("popularity") var popularity: Double? = null,
    @JsonProperty("first_air_date") var firstAirDate: String? = null,
    @JsonProperty("vote_average") var voteAverage: Double? = null,
    @JsonProperty("vote_count") var voteCount: Int? = null,
    @JsonProperty("origin_country") var originCountry: ArrayList<String> = arrayListOf()
)
