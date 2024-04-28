package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.screens.details.DetailsUiModel.WatchProvider.Companion.toUiModel
import com.free.tvtracker.tracked.response.TmdbShowStatus
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.TmdbConfigData

class ShowUiModelMapper(
    private val seasonUiModelMapper: ShowSeasonUiModelMapper,
    private val castMapper: ShowCastUiModelMapper,
) : MapperWithOptions<TmdbShowDetailsApiModel, DetailsUiModel, TrackedShowApiModel?> {

    override fun map(from: TmdbShowDetailsApiModel, options: TrackedShowApiModel?): DetailsUiModel {
        val ongoing =
            if (from.status == TmdbShowStatus.RETURNING.status) "Ongoing" else from.lastAirDate?.substring(0, 4)
        val episodeCount = from.seasons.sumOf { it.episodeCount ?: 0 }
        val seasonsInfo = "${from.seasons.size} seasons - $episodeCount episodes total"
        return DetailsUiModel(
            tmdbId = from.id,
            name = from.name,
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            releaseStatus = "${from.firstAirDate?.substring(0, 4)} - $ongoing",
            trackingStatus = "currently watching",
            description = from.overview,
            seasonsInfo = seasonsInfo,
            seasons = from.seasons.map { seasonUiModelMapper.map(it, ShowSeasonUiModelMapper.O(from.id, options)) },
            castFirst = castMapper.map(from.cast?.getOrNull(0)),
            castSecond = castMapper.map(from.cast?.getOrNull(1)),
            watchProviders = from.watchProvider?.map { it.toUiModel() } ?: emptyList()
        )
    }
}
