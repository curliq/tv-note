package com.free.tvtracker.details.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieDetailsApiRequestBody(
    @SerialName("tmdb_id")
    val tmdbId: Int,
    @SerialName("country_code")
    val countryCode: String
)
