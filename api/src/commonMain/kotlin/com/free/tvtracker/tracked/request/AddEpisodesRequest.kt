package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddEpisodesRequest(
    @SerialName("trackedShowId") val trackedShowId: Int,
    @SerialName("episode_ids") val episodeIds: List<String>
)
