package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.tracked.response.TrackedShowApiModel

typealias EpisodeApi = TmdbShowDetailsApiModel.Season.Episode
typealias EpisodeUi = DetailsUiModel.Season.Episode

class ShowSeasonUiModelMapper(
    private val episodeMapper: ShowEpisodeUiModelMapper,
) : MapperWithOptions<TmdbShowDetailsApiModel.Season, DetailsUiModel.Season, ShowSeasonUiModelMapper.O> {

    data class O(val tmdbShowId: Int, val trackedShowApiModel: TrackedShowApiModel?)

    override fun map(from: TmdbShowDetailsApiModel.Season, options: O): DetailsUiModel.Season {
        val eps = from.episodes?.map { episodeMapper.map(it, options) }?.sortedBy { it.number } ?: emptyList()
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
