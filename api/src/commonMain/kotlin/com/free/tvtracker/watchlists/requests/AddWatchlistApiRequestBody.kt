package com.free.tvtracker.watchlists.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddWatchlistApiRequestBody(
    @SerialName("name") val name: String,
)
