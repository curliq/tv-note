package com.free.tvtracker.domain

import com.free.tvtracker.tracked.response.TrackedShowApiModel

class GetWatchlistedShowsUseCase {
    operator fun invoke(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        return shows.filter { it.watchlisted }
    }
}
