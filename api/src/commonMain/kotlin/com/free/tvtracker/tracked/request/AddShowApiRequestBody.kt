package com.free.tvtracker.tracked.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddShowApiRequestBody(
    @SerialName("tmdb_id") val tmdbShowId: Int,
    @SerialName("watchlisted") val watchlisted: Boolean,
    /**
     * also add the show's episodes as tracked, basically adding a show to the "Finished" tab
     */
    @SerialName("add_all_episodes") val addAllEpisodes: Boolean
)
