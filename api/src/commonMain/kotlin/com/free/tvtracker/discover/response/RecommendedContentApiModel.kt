package com.free.tvtracker.discover.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedContentApiModel(
    @SerialName("results")
    val results: List<Data>,
    @SerialName("related")
    val relatedContent: List<RelatedContent>
) {
    @Serializable
    data class RelatedContent(val tmdbId: Int)

    @Serializable
    data class Data(
        @SerialName("id") val tmdbId: Int,
        @SerialName("name") val name: String,
        @SerialName("poster_path") val posterPath: String? = null,
        @SerialName("genre_ids") val genreIds: List<Int>? = null,
        @SerialName("popularity") val popularity: Double? = null,
        @SerialName("first_air_date") val firstAirDate: String? = null,
        @SerialName("vote_average") val voteAverage: Double? = null,
        @SerialName("vote_count") val voteCount: Int? = null,
    )
}
