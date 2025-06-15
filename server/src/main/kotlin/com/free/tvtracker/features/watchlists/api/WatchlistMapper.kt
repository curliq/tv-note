package com.free.tvtracker.features.watchlists.api

import com.free.tvtracker.features.watchlists.data.WatchlistEntity
import com.free.tvtracker.watchlists.response.WatchlistApiModel

fun WatchlistEntity.toApiModel(): WatchlistApiModel {
    return WatchlistApiModel(
        id = this.id,
        name = this.name,
        showsCount = this.shows.size,
        moviesCount = this.movies.size,
    )
}
