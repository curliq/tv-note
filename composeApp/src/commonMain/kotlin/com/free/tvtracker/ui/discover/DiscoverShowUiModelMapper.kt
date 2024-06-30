package com.free.tvtracker.ui.discover

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.search.response.SmallShowApiModel
import com.free.tvtracker.ui.common.TmdbConfigData

class DiscoverShowUiModelMapper : Mapper<SmallShowApiModel, DiscoverUiModel.Content> {
    override fun map(from: SmallShowApiModel): DiscoverUiModel.Content {
        return DiscoverUiModel.Content(
            tmdbId = from.tmdbId,
            title = from.name,
            image = TmdbConfigData.get().getPosterUrl(from.posterPath),
        )
    }
}
