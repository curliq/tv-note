package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.constants.TmdbVideoType
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.ui.common.TmdbConfigData

class DetailsUiModelForShowMapper(
    private val seasonUiModelMapper: ShowSeasonUiModelMapper,
    private val castMapper: ShowCastUiModelMapper,
    private val crewMapper: ShowCrewUiModelMapper,
    private val showWatchProviderUiModelMapper: ShowWatchProviderUiModelMapper,
    private val showVideoUiModelMapper: ShowVideoUiModelMapper,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val getShowStatusUseCase: GetShowStatusUseCase,
    private val locationService: CachingLocationService,
    private val ratingsUiModelMapper: DetailsRatingsUiModelMapper,
) : MapperWithOptions<TmdbShowDetailsApiModel, DetailsUiModel, TrackedContentApiModel?> {

    override fun map(from: TmdbShowDetailsApiModel, options: TrackedContentApiModel?): DetailsUiModel {
        val clipsAndOtherVideos = from.videos?.filter {
            it.type in listOf(
                TmdbVideoType.CLIP.type,
                TmdbVideoType.FEATURETTE.type,
                TmdbVideoType.OPENING_CREDITS.type
            )
        }
        val mediaVideosTeasers = from.videos?.filter { it.type == TmdbVideoType.TEASER.type }
        return DetailsUiModel(
            isTvShow = true,
            tmdbId = from.id,
            name = from.name,
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            releaseStatus = getShowStatusUseCase(from.status, from.firstAirDate, from.lastAirDate),
            duration = null,
            trackingStatus = getTrackingStatus(options),
            trackedContentId = options?.tvShow?.id,
            homepageUrl = from.homepage,
            description = from.overview,
            genres = from.genres,
            seasonsInfo =
            "${from.seasons.size} ${if (from.seasons.size == 1) "season" else "seasons"} - " +
                "${from.seasons.sumOf { it.episodeCount ?: 0 }} episodes total", // eg: "1 season - 10 episodes total"
            seasons = from.seasons.map { seasonUiModelMapper.map(it, ShowSeasonUiModelMapper.O(from.id, options)) },
            movieSeries = null,
            castFirst = castMapper.map(from.cast?.getOrNull(0)),
            castSecond = castMapper.map(from.cast?.getOrNull(1)),
            cast = from.cast?.map { castMapper.map(it) } ?: emptyList(),
            watchProviders = from.watchProvider?.map { showWatchProviderUiModelMapper.map(it) } ?: emptyList(),
            crew = from.crew?.map { crewMapper.map(it) } ?: emptyList(),
            watchProviderCountry = locationService.countryName(),
            mediaTrailer = (from.videos?.firstOrNull { it.type == TmdbVideoType.TRAILER.type }
                ?: mediaVideosTeasers?.firstOrNull()
                ?: clipsAndOtherVideos?.firstOrNull())
                ?.run { showVideoUiModelMapper.map(this) },
            mediaVideosTrailers = from.videos?.filter { it.type == TmdbVideoType.TRAILER.type }
                ?.mapNotNull { showVideoUiModelMapper.map(it) } ?: emptyList(),
            mediaVideosTeasers = (mediaVideosTeasers ?: emptyList()).mapNotNull { showVideoUiModelMapper.map(it) },
            mediaVideosBehindTheScenes = from.videos?.filter { it.type == TmdbVideoType.BEHIND_THE_SCENES.type }
                ?.mapNotNull { showVideoUiModelMapper.map(it) } ?: emptyList(),
            mediaVideosClipsAndOther = clipsAndOtherVideos?.mapNotNull { showVideoUiModelMapper.map(it) }
                ?: emptyList(),
            mediaMostPopularImage = TmdbConfigData.get()
                .getBackdropUrl(from.images?.backdrops?.firstOrNull()?.filePath),
            mediaImagesPosters = from.images?.posters?.map { TmdbConfigData.get().getPosterUrl(it.filePath) }
                ?: emptyList(),
            mediaImagesBackdrops = from.images?.backdrops?.map { TmdbConfigData.get().getBackdropUrl(it.filePath) }
                ?: emptyList(),
            ratingTmdbVoteAverage = ratingsUiModelMapper.formatVoteAverage(from.voteAverage),
            ratingTmdbVoteCount = ratingsUiModelMapper.formatVoteCount(from.voteCount ?: 0),
            omdbRatings = null,
            reviews = null,
            budget = "",
            revenue = "",
            website = from.homepage ?: "(no website)"
        )
    }

    private fun getTrackingStatus(trackedShow: TrackedContentApiModel?): DetailsUiModel.TrackingStatus {
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
}
