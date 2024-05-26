package com.free.tvtracker.screens.discover

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.TrendingShowApiModel
import com.free.tvtracker.utils.TmdbConfigData

class DiscoverShowUiModelMapper : Mapper<TrendingShowApiModel.Data, DiscoverUiModel.Content> {
    override fun map(from: TrendingShowApiModel.Data): DiscoverUiModel.Content {
        return DiscoverUiModel.Content(
            tmdbId = from.tmdbId,
            title = from.name,
            image = TmdbConfigData.get().getPosterUrl(from.posterPath),
        )
    }
}
