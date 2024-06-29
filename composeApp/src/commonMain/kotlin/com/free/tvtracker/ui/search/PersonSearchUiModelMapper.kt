package com.free.tvtracker.ui.search

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.search.response.SearchMultiApiModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.tracked.response.buildPseudoId
import com.free.tvtracker.ui.common.TmdbConfigData

class PersonSearchUiModelMapper :
    MapperWithOptions<SearchMultiApiModel, AddTrackedItemUiModel, SearchUiModelMapperOptions> {
    override fun map(from: SearchMultiApiModel, options: SearchUiModelMapperOptions): AddTrackedItemUiModel {
        return AddTrackedItemUiModel(
            from.tmdbId,
            buildPseudoId(TrackedContentApiModel.ContentType.Person, from.tmdbId),
            from.name!!,
            TmdbConfigData.get().getPosterUrl(from.profilePath),
            options.tracked,
            AddTrackedItemUiModel.TrackAction.None,
            isTvShow = false,
            isPerson = true
        )
    }
}
