package com.free.tvtracker.ui.discover

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.search.response.SmallMovieApiModel
import com.free.tvtracker.ui.common.TmdbConfigData

class DiscoverMovieUiModelMapper : Mapper<SmallMovieApiModel, DiscoverUiModel.Content> {
    override fun map(from: SmallMovieApiModel): DiscoverUiModel.Content {
        return DiscoverUiModel.Content(
            tmdbId = from.tmdbId,
            title = from.title,
            image = TmdbConfigData.get().getPosterUrl(from.posterPath),
            isTvShow = false
        )
    }
}
