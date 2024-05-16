package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.screens.details.DetailsUiModel.Crew
import com.free.tvtracker.utils.TmdbConfigData

class ShowCrewUiModelMapper : Mapper<TmdbShowDetailsApiModel.Crew?, Crew> {
    override fun map(from: TmdbShowDetailsApiModel.Crew?): Crew {
        return Crew(
            irlName = from?.name ?: "",
            job = from?.job ?: "",
            photo = TmdbConfigData.get().getPosterUrl(from?.profilePath ?: "")
        )
    }
}
