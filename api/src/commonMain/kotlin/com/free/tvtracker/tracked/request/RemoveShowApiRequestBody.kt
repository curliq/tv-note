package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoveShowApiRequestBody(
    @SerialName("tracked_show_id") val trackedShowId: Int
)
