package com.free.tvtracker.ui.search

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.search.response.SearchMultiApiModel
import com.free.tvtracker.ui.common.TmdbConfigData

class MovieSearchUiModelMapper :
    MapperWithOptions<SearchMultiApiModel, AddTrackedItemUiModel, SearchUiModelMapperOptions> {
    override fun map(from: SearchMultiApiModel, options: SearchUiModelMapperOptions): AddTrackedItemUiModel {
        val titleAppend = if (options.originScreen == AddTrackedScreenOriginScreen.Watching) " (Movie)" else ""
        return AddTrackedItemUiModel(
            from.tmdbId,
            from.title + titleAppend,
            TmdbConfigData.get().getPosterUrl(from.posterPath),
            options.tracked,
            when (options.originScreen) {
                AddTrackedScreenOriginScreen.Watching -> AddTrackedItemUiModel.TrackAction.Watchlist
                AddTrackedScreenOriginScreen.Finished -> AddTrackedItemUiModel.TrackAction.Watchlist
                AddTrackedScreenOriginScreen.Watchlist -> AddTrackedItemUiModel.TrackAction.Watchlist
                AddTrackedScreenOriginScreen.Discover -> AddTrackedItemUiModel.TrackAction.None
            }
        )
    }
}
