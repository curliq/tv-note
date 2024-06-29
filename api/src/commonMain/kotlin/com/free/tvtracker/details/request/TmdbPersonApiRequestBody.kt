package com.free.tvtracker.details.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbPersonApiRequestBody(@SerialName("tmdb_id") val tmdbId: Int)
