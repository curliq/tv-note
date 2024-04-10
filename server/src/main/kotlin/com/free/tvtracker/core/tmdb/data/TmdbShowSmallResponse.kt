package com.free.tvtracker.core.tmdb.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbShowSmallResponse(
    @SerialName("adult") var adult: Boolean? = null,
    @SerialName("backdrop_path") var backdropPath: String? = null,
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("original_language") var originalLanguage: String? = null,
    @SerialName("original_name") var originalName: String? = null,
    @SerialName("overview") var overview: String? = null,
    @SerialName("poster_path") var posterPath: String? = null,
    @SerialName("media_type") var mediaType: String? = null,
    @SerialName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @SerialName("popularity") var popularity: Double? = null,
    @SerialName("first_air_date") var firstAirDate: String? = null,
    @SerialName("vote_average") var voteAverage: Double? = null,
    @SerialName("vote_count") var voteCount: Int? = null,
    @SerialName("origin_country") var originCountry: ArrayList<String> = arrayListOf()
)
