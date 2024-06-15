package com.free.tvtracker.ui.finished

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.ui.common.TmdbConfigData

class FinishedShowUiModelMapper(
    private val getShowStatusUseCase: GetShowStatusUseCase,
) : Mapper<TrackedShowApiModel, FinishedShowUiModel> {
    override fun map(from: TrackedShowApiModel): FinishedShowUiModel {
        return FinishedShowUiModel(
            tmdbId = from.storedShow.tmdbId,
            title = from.storedShow.title,
            image = TmdbConfigData.get().getPosterUrl(from.storedShow.posterImage),
            status = getShowStatusUseCase(
                from.storedShow.status,
                from.storedShow.storedEpisodes.firstOrNull()?.airDate,
                from.storedShow.storedEpisodes.lastOrNull()?.airDate
            )
        )
    }

}
