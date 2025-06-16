package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.ui.details.DetailsUiModel.Cast
import com.free.tvtracker.ui.common.TmdbConfigData

class ContentShowCastUiModelMapper : Mapper<TmdbShowDetailsApiModel.Cast?, Cast> {
    override fun map(from: TmdbShowDetailsApiModel.Cast?): Cast {
        return Cast(
            tmdbId = from?.id ?: 0,
            irlName = from?.name ?: "",
            characterName = from?.character ?: "",
            photo = TmdbConfigData.get().getPosterUrl(from?.profilePath ?: "")
        )
    }
}
