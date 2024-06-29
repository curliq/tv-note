package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetShowWatchlistedApiRequestBody(
    @SerialName("trackedshow_id")
    val trackedContentId: Int,
    val watchlisted: Boolean,
    @SerialName("is_tvshow")
    val isTvShow: Boolean
)
