package com.free.tvtracker.domain

import com.free.tvtracker.tracked.response.TrackedContentApiModel

class GetWatchlistedShowsUseCase {
    operator fun invoke(shows: List<TrackedContentApiModel>): List<TrackedContentApiModel> {
        return shows.filter { it.watchlisted }
    }
}
