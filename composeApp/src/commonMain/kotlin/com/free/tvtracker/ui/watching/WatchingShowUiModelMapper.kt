package com.free.tvtracker.ui.watching

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class WatchingShowUiModelMapper(
    private val getNextUnwatchedEpisodeUseCase: GetNextUnwatchedEpisodeUseCase,
) : Mapper<TrackedContentApiModel, WatchingItemUiModel> {
    override fun map(from: TrackedContentApiModel): WatchingItemUiModel {
        val nextEpisode = getNextUnwatchedEpisodeUseCase(from)
        val airText = try {
            val date = LocalDate.parse(input = nextEpisode?.airDate ?: "")
            val day = date.dayOfMonth
            val suffix = when {
                day in 11..13 -> "th" // Special case for 11, 12, 13
                day % 10 == 1 -> "st"
                day % 10 == 2 -> "nd"
                day % 10 == 3 -> "rd"
                else -> ""
            }
            "Available on " +
                date.format(LocalDate.Format {
                    dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED); char(' ')
                    dayOfMonth(padding = Padding.NONE); chars(suffix); char(' ')
                    monthName(MonthNames.ENGLISH_ABBREVIATED); char(' ')
                    year()
                })
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            "date unavailable"
        }
        return WatchingItemUiModel(
            trackedShowId = from.tvShow!!.id,
            tmdbId = from.tvShow!!.storedShow.tmdbId,
            title = from.tvShow!!.storedShow.title,
            image = TmdbConfigData.get().getPosterUrl(from.tvShow!!.storedShow.posterImage),
            nextEpisode = nextEpisode?.run {
                WatchingItemUiModel.NextEpisode(
                    id = this.id,
                    watchNext = "Watch next: ",
                    season = "S${season} ",
                    seasonNumber = season,
                    episode = "E${episode}",
                    episodeNumber = episode,
                )
            },
            nextEpisodeCountdown = airText
        )
    }
}
