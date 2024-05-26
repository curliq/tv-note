package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddShowRequest(
    @SerialName("tmdb_id") val tmdbShowId: Int,
    @SerialName("watchlisted") val watchlisted: Boolean
)
