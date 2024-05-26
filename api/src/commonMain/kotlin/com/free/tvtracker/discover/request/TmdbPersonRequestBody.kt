package com.free.tvtracker.discover.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbPersonRequestBody(@SerialName("tmdb_id") val tmdbId: Int)
