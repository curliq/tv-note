package com.free.tvtracker.ui.search

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.search.response.SearchShowApiModel
import com.free.tvtracker.utils.TmdbConfigData

class ShowSearchUiModelMapper : MapperWithOptions<SearchShowApiModel, AddTrackedItemUiModel, Boolean> {
    override fun map(from: SearchShowApiModel, options: Boolean): AddTrackedItemUiModel {
        return AddTrackedItemUiModel(
            from.tmdbId,
            from.name,
            TmdbConfigData.get().getPosterUrl(from.posterPath),
            options
        )
    }
}
