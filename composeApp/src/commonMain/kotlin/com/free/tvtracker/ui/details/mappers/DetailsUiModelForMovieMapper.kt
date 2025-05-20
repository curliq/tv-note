package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.constants.TmdbVideoType
import com.free.tvtracker.details.response.TmdbMovieDetailsApiModel
import com.free.tvtracker.expect.CommonStringUtils
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData
import com.free.tvtracker.ui.details.DetailsUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class DetailsUiModelForMovieMapper(
    private val castMapper: ShowCastUiModelMapper,
    private val crewMapper: ShowCrewUiModelMapper,
    private val showWatchProviderUiModelMapper: ShowWatchProviderUiModelMapper,
    private val showVideoUiModelMapper: ShowVideoUiModelMapper,
    private val locationService: CachingLocationService,
    private val ratingsUiModelMapper: DetailsRatingsUiModelMapper,
    private val stringUtils: CommonStringUtils = CommonStringUtils(),
) : MapperWithOptions<TmdbMovieDetailsApiModel, DetailsUiModel, TrackedContentApiModel?> {
    override fun map(from: TmdbMovieDetailsApiModel, options: TrackedContentApiModel?): DetailsUiModel {
        val airText = try {
            LocalDate.parse(input = from.releaseDate ?: "").format(LocalDate.Format {
                dayOfMonth(padding = Padding.NONE); char(' ')
                monthName(MonthNames.ENGLISH_FULL); char(' ')
                year()
            })
        } catch (e: IllegalArgumentException) {
            "date unavailable"
        }
        val duration = "${(from.runtime ?: 0) / 60}h ${(from.runtime ?: 0) % 60}m"
        val clipsAndOtherVideos = from.videos?.filter {
            it.type in listOf(
                TmdbVideoType.CLIP.type,
                TmdbVideoType.FEATURETTE.type,
                TmdbVideoType.OPENING_CREDITS.type
            )
        }
        val mediaVideosTeasers = from.videos?.filter { it.type == TmdbVideoType.TEASER.type }
        return DetailsUiModel(
            isTvShow = false,
            tmdbId = from.id,
            name = from.title!!,
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            releaseStatus = airText,
            duration = duration,
            trackingStatus = getTrackingStatus(options),
            trackedContentId = options?.movie?.id,
            homepageUrl = from.homepage,
            description = from.overview,
            genres = from.genres,
            seasonsInfo = null,
            seasons = null,
            movieSeries = from.belongsToCollection?.let {
                DetailsUiModel.MovieSeries(
                    overview = it.overview,
                    movies = it.movies.map {
                        DetailsUiModel.MovieSeries.Movie(
                            tmdbId = it.tmdbId,
                            posterUrl = TmdbConfigData.get().getPosterUrl(it.posterPath),
                            name = it.title,
                            year = try {
                                LocalDate.parse(input = it.releaseDate ?: "").format(LocalDate.Format {
                                    year()
                                })
                            } catch (e: IllegalArgumentException) {
                                "no date"
                            }
                        )
                    }
                )
            },
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
            budget = formatMoney(from.budget) ?: "(not available)",
            revenue = formatMoney(from.revenue) ?: "(not available)",
            website = from.homepage
        )
    }

    private fun formatMoney(amount: Double?): String? {
        if (amount == null) return null
        val language = locationService.languageCode()
        val countryCode = locationService.getCountryCode()
        return stringUtils.formatMoney(d = amount, language = language, countryCode = countryCode)
    }

    private fun getTrackingStatus(trackedShow: TrackedContentApiModel?): DetailsUiModel.TrackingStatus {
        if (trackedShow == null) {
            return DetailsUiModel.TrackingStatus(
                action1 = DetailsUiModel.TrackingStatus.Action.TrackWatchlist,
                action2 = null,
            )
        } else {
            return if (trackedShow.watchlisted) {
                DetailsUiModel.TrackingStatus(
                    action1 = DetailsUiModel.TrackingStatus.Action.MoveMovieToFinished,
                    action2 = DetailsUiModel.TrackingStatus.Action.RemoveFromWatchlist,
                )
            } else {
                DetailsUiModel.TrackingStatus(
                    action1 = null,
                    action2 = DetailsUiModel.TrackingStatus.Action.RemoveMovieFromWatched
                )
            }
        }
    }
}
