package com.free.tvtracker.watchlists.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RenameWatchlistApiRequestBody(
    @SerialName("watchlist_id") val watchlistId: Int,
    @SerialName("name") val name: String,
)
