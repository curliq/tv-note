package com.free.tvtracker.watchlists.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistApiModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("tv_show_count") val showsCount: Int,
    @SerialName("movie_count") val moviesCount: Int
) {
    companion object {
        const val WATCHLIST_LIST_ID = Int.MAX_VALUE
        const val FINISHED_LIST_ID = Int.MAX_VALUE -1
    }
}

