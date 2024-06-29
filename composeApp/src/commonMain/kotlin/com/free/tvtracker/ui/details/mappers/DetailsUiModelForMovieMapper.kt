package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.constants.TmdbVideoType
import com.free.tvtracker.details.response.TmdbMovieDetailsApiModel
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.expect.CommonStringUtils
import com.free.tvtracker.expect.data.CachingLocationService
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlin.math.ln
import kotlin.math.pow

class DetailsUiModelForMovieMapper(
    private val castMapper: ShowCastUiModelMapper,
    private val crewMapper: ShowCrewUiModelMapper,
    private val showWatchProviderUiModelMapper: ShowWatchProviderUiModelMapper,
    private val showVideoUiModelMapper: ShowVideoUiModelMapper,
    private val locationService: CachingLocationService,
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
        return DetailsUiModel(
            isTvShow = false,
            tmdbId = from.id,
            homepageUrl = from.homepage,
            name = from.title!!,
            posterUrl = TmdbConfigData.get().getPosterUrl(from.posterPath),
            releaseStatus = airText,
            duration = duration,
            trackingStatus = getTrackingStatus(options),
            trackedContentId = options?.movie?.id,
            description = from.overview,
            genres = from.genres.joinToString(", "),
            seasonsInfo = null,
            seasons = null,
            castFirst = castMapper.map(from.cast?.getOrNull(0)),
            castSecond = castMapper.map(from.cast?.getOrNull(1)),
            cast = from.cast?.map { castMapper.map(it) } ?: emptyList(),
            crew = from.crew?.map { crewMapper.map(it) } ?: emptyList(),
            watchProviders = from.watchProvider?.map { showWatchProviderUiModelMapper.map(it) } ?: emptyList(),
            watchProviderCountry = locationService.countryName(),
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
