package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddMovieApiRequestBody(
    @SerialName("tmdb_id") val tmdbMovieId: Int,
    @SerialName("watchlisted") val watchlisted: Boolean
)
