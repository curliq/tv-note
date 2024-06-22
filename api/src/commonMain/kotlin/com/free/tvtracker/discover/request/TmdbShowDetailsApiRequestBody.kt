package com.free.tvtracker.discover.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbShowDetailsApiRequestBody(
    @SerialName("tmdb_id") val tmdbId: Int,
    @SerialName("include_episodes") val includeEpisodes: Boolean,
    @SerialName("country_code") val countryCode: String,
)
