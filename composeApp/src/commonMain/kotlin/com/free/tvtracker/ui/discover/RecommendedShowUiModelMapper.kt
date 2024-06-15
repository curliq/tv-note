package com.free.tvtracker.ui.discover

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.RecommendedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData

class RecommendedShowUiModelMapper : Mapper<RecommendedContentApiModel.Data, DiscoverUiModel.Content> {
    override fun map(from: RecommendedContentApiModel.Data): DiscoverUiModel.Content {
        return DiscoverUiModel.Content(
            tmdbId = from.tmdbId,
            title = from.name,
            image = TmdbConfigData.get().getPosterUrl(from.posterPath),
        )
    }
}
