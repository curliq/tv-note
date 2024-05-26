package com.free.tvtracker.discover.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TrendingShowApiModel(
    @SerialName("page")
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val results: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("id") val tmdbId: Int,
        @SerialName("name") val name: String,
        @SerialName("poster_path") val posterPath: String? = null,
        @SerialName("overview") @Transient val overview: String? = null,
        @SerialName("original_language") val originalLanguage: String? = null,
        @SerialName("original_name") val originalName: String? = null,
        @SerialName("genre_ids") val genreIds: List<Int>? = null,
        @SerialName("popularity") val popularity: Double? = null,
        @SerialName("first_air_date") val firstAirDate: String? = null,
        @SerialName("vote_average") val voteAverage: Double? = null,
        @SerialName("vote_count") val voteCount: Int? = null,
        @SerialName("origin_country") val originCountry: List<String>? = null
    )
}
