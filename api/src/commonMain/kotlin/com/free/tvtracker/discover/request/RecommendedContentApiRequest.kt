package com.free.tvtracker.discover.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedContentApiRequest(
    @SerialName("related_shows_tmdb_ids")
    val relatedShowsTmdbIds: List<Int>
)
