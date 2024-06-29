package com.free.tvtracker.ui.search

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.search.response.SearchMultiApiModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.tracked.response.buildPseudoId
import com.free.tvtracker.ui.common.TmdbConfigData

class ShowSearchUiModelMapper :
    MapperWithOptions<SearchMultiApiModel, AddTrackedItemUiModel, SearchUiModelMapperOptions> {
    override fun map(from: SearchMultiApiModel, options: SearchUiModelMapperOptions): AddTrackedItemUiModel {
        return AddTrackedItemUiModel(
            from.tmdbId,
            buildPseudoId(TrackedContentApiModel.ContentType.TvShow, from.tmdbId),
            from.name!!,
            TmdbConfigData.get().getPosterUrl(from.posterPath),
            options.tracked,
            when (options.originScreen) {
                AddTrackedScreenOriginScreen.Watching -> AddTrackedItemUiModel.TrackAction.Watching
                AddTrackedScreenOriginScreen.Finished ->  AddTrackedItemUiModel.TrackAction.Finished(true)
                AddTrackedScreenOriginScreen.Watchlist ->  AddTrackedItemUiModel.TrackAction.Watchlist(true)
                AddTrackedScreenOriginScreen.Discover ->  AddTrackedItemUiModel.TrackAction.None
            },
            isTvShow = true,
            isPerson = false
        )
    }
}
