package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.screens.details.DetailsUiModel.Cast
import com.free.tvtracker.utils.TmdbConfigData

class ShowCastUiModelMapper : Mapper<TmdbShowDetailsApiModel.Cast?, Cast> {
    override fun map(from: TmdbShowDetailsApiModel.Cast?): Cast {
        return Cast(
            irlName = from?.name ?: "",
            characterName = from?.character ?: "",
            photo = TmdbConfigData.get().getPosterUrl(from?.profilePath ?: "")
        )
    }
}
