package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddEpisodesRequest(
    @SerialName("episodes") val episodes: List<Episode>
) {
    @Serializable
    data class Episode(
        @SerialName("tracked_show_id") val trackedShowId: Int,
        @SerialName("episode_id") val episodeId: Int
    )
}
