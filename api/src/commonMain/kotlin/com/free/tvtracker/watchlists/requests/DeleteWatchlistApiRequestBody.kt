package com.free.tvtracker.watchlists.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteWatchlistApiRequestBody(
    @SerialName("watchlist_id") val watchlistId: Int
)
