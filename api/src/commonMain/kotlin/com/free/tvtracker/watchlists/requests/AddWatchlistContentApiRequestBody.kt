package com.free.tvtracker.watchlists.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddWatchlistContentApiRequestBody(
    @SerialName("tracked_content_id") val trackedContentId: Int,
    @SerialName("watchlist_id") val watchlistId: Int,
    @SerialName("is_tv_show") val isTvShow: Boolean
)
