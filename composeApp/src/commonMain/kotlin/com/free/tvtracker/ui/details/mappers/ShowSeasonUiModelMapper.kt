package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel

typealias EpisodeApi = TmdbShowDetailsApiModel.Season.Episode
typealias EpisodeUi = DetailsUiModel.Season.Episode

class ShowSeasonUiModelMapper(
    private val episodeMapper: ShowEpisodeUiModelMapper,
) : MapperWithOptions<TmdbShowDetailsApiModel.Season, DetailsUiModel.Season, ShowSeasonUiModelMapper.O> {

    data class O(val tmdbShowId: Int, val trackedContentApiModel: TrackedContentApiModel?)

    override fun map(from: TmdbShowDetailsApiModel.Season, options: O): DetailsUiModel.Season {
        val eps = from.episodes?.sortedBy { it.number }?.map { episodeMapper.map(it, options) } ?: emptyList()
        return DetailsUiModel.Season(
            seasonId = from.id,
            tmdbShowId = options.tmdbShowId,
            seasonTitle = from.name ?: "",
            episodes = eps,
            watched = eps.isNotEmpty() && eps.all { it.watched },
            isWatchable = eps.any { it.isWatchable && !it.watched }
        )
    }

}
