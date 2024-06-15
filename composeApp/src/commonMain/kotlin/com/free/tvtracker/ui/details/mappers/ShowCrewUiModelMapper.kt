package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.ui.details.DetailsUiModel.Crew
import com.free.tvtracker.utils.TmdbConfigData

class ShowCrewUiModelMapper : Mapper<TmdbShowDetailsApiModel.Crew?, Crew> {
    override fun map(from: TmdbShowDetailsApiModel.Crew?): Crew {
        return Crew(
            tmdbId = from?.id ?: 0,
            irlName = from?.name ?: "",
            job = from?.job ?: "",
            photo = TmdbConfigData.get().getPosterUrl(from?.profilePath ?: "")
        )
    }
}
