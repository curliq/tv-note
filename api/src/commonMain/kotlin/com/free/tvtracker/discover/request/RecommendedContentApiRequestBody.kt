package com.free.tvtracker.discover.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedContentApiRequestBody(
    @SerialName("related_content_tmdb_ids")
    val relatedContentTmdbIds: List<Int>
)
