package com.free.tvtracker.ui.watchlist

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.TmdbConfigData

class WatchlistShowUiModelMapper(
    private val getShowStatusUseCase: GetShowStatusUseCase,
) : Mapper<TrackedShowApiModel, WatchlistShowUiModel> {
    override fun map(from: TrackedShowApiModel): WatchlistShowUiModel {
        return WatchlistShowUiModel(
            tmdbId = from.storedShow.tmdbId,
            title = from.storedShow.title,
            image = TmdbConfigData.get().getPosterUrl(from.storedShow.posterImage),
            status = getShowStatusUseCase(
                from.storedShow.status,
                from.storedShow.storedEpisodes.firstOrNull()?.airDate,
                from.storedShow.storedEpisodes.lastOrNull()?.airDate
            )
        )
    }

}
