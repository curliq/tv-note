package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.constants.TmdbVideoType
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.CommonStringUtils
import com.free.tvtracker.utils.TmdbConfigData
import kotlin.math.ln
import kotlin.math.pow

class ShowUiModelMapper(
    private val seasonUiModelMapper: ShowSeasonUiModelMapper,
    private val castMapper: ShowCastUiModelMapper,
    private val crewMapper: ShowCrewUiModelMapper,
    private val showWatchProviderUiModelMapper: ShowWatchProviderUiModelMapper,
    private val showVideoUiModelMapper: ShowVideoUiModelMapper,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val getShowStatusUseCase: GetShowStatusUseCase,
    private val stringUtils: CommonStringUtils = CommonStringUtils(),
) : MapperWithOptions<TmdbShowDetailsApiModel, DetailsUiModel, TrackedShowApiModel?> {

    override fun map(from: TmdbShowDetailsApiModel, options: TrackedShowApiModel?): DetailsUiModel {
        return DetailsUiModel(
            tmdbId = from.id,
            homepageUrl = from.homepage,
            name = from.name,
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            releaseStatus = getShowStatusUseCase(from.status, from.firstAirDate, from.lastAirDate),
            trackingStatus = getTrackingStatus(options),
            description = from.overview,
            genres = from.genres.joinToString(", "),
            seasonsInfo =
            "${from.seasons.size} seasons - ${from.seasons.sumOf { it.episodeCount ?: 0 }} episodes total",
            seasons = from.seasons.map { seasonUiModelMapper.map(it, ShowSeasonUiModelMapper.O(from.id, options)) },
            castFirst = castMapper.map(from.cast?.getOrNull(0)),
            castSecond = castMapper.map(from.cast?.getOrNull(1)),
            cast = from.cast?.map { castMapper.map(it) } ?: emptyList(),
            crew = from.crew?.map { crewMapper.map(it) } ?: emptyList(),
            watchProviders = from.watchProvider?.map { showWatchProviderUiModelMapper.map(it) } ?: emptyList(),
            mediaTrailer = from.videos?.firstOrNull { it.type == TmdbVideoType.TRAILER.type }
                ?.run { showVideoUiModelMapper.map(this) },
            mediaVideosTrailers = from.videos?.filter { it.type == TmdbVideoType.TRAILER.type }
                ?.mapNotNull { showVideoUiModelMapper.map(it) } ?: emptyList(),
            mediaVideosTeasers = from.videos?.filter { it.type == TmdbVideoType.TEASER.type }
                ?.mapNotNull { showVideoUiModelMapper.map(it) } ?: emptyList(),
            mediaVideosBehindTheScenes = from.videos?.filter { it.type == TmdbVideoType.BEHIND_THE_SCENES.type }
                ?.mapNotNull { showVideoUiModelMapper.map(it) } ?: emptyList(),
            mediaMostPopularImage = TmdbConfigData.get()
                .getBackdropUrl(from.images?.backdrops?.firstOrNull()?.filePath),
            mediaImagesPosters = from.images?.posters?.map { TmdbConfigData.get().getPosterUrl(it.filePath) }
                ?: emptyList(),
            mediaImagesBackdrops = from.images?.backdrops?.map { TmdbConfigData.get().getBackdropUrl(it.filePath) }
                ?: emptyList(),
            ratingTmdbVoteCount = formatVoteCount(from.voteCount ?: 0),
            ratingTmdbVoteAverage = stringUtils.roundDouble((from.voteAverage ?: 0.toDouble()), 1) + "/10",
        )
    }

    private fun getTrackingStatus(trackedShow: TrackedShowApiModel?): DetailsUiModel.TrackingStatus {
        if (trackedShow == null) {
            return DetailsUiModel.TrackingStatus(
                action1 = DetailsUiModel.TrackingStatus.Action.TrackWatching,
                action2 = DetailsUiModel.TrackingStatus.Action.TrackWatchlist,
            )
        } else {
            if (trackedShow.watchlisted) {
                return DetailsUiModel.TrackingStatus(
                    action1 = DetailsUiModel.TrackingStatus.Action.MoveToWatching,
                    action2 = DetailsUiModel.TrackingStatus.Action.RemoveFromWatchlist,
                )
            } else {
                return if (isTrackedShowWatchableUseCase.watchable(listOf(trackedShow)).isNotEmpty()) {
                    DetailsUiModel.TrackingStatus(
                        action1 = DetailsUiModel.TrackingStatus.Action.MoveToWatchlist,
                        action2 = DetailsUiModel.TrackingStatus.Action.RemoveFromWatching
                    )
                } else {
                    DetailsUiModel.TrackingStatus(
                        action1 = null,
                        action2 = DetailsUiModel.TrackingStatus.Action.RemoveFromWatching
                    )
                }
            }
        }
    }

    private fun formatVoteCount(count: Int): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        val shortenChar = count / 1000.0.pow(exp.toDouble())
        return stringUtils.shortenDouble(
            d = shortenChar,
            shortenCharacter = "kMGTPE"[exp - 1],
            decimalPoints = 1
        )
    }
}
