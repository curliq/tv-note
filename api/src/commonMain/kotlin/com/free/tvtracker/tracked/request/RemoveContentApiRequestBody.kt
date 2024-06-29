package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoveContentApiRequestBody(
    @SerialName("tracked_content_id") val trackedContentId: Int,
    @SerialName("is_tvshow") val isTvShow: Boolean,
)
