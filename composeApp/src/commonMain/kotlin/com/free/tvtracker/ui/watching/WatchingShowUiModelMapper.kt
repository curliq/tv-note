package com.free.tvtracker.ui.watching

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class WatchingShowUiModelMapper(
    private val getNextUnwatchedEpisodeUseCase: GetNextUnwatchedEpisodeUseCase,
) : Mapper<TrackedShowApiModel, WatchingItemUiModel> {
    override fun map(from: TrackedShowApiModel): WatchingItemUiModel {
        val nextEpisode = getNextUnwatchedEpisodeUseCase(from)
        val airText = try {
            LocalDate.parse(input = nextEpisode?.airDate ?: "").format(LocalDate.Format {
                dayOfMonth(padding = Padding.NONE); char(' ')
                monthName(MonthNames.ENGLISH_FULL); char(' ')
                year()
            })
        } catch (e: IllegalArgumentException) {
            "date unavailable"
        }
        return WatchingItemUiModel(
            trackedShowId = from.id,
            tmdbId = from.storedShow.tmdbId,
            title = from.storedShow.title,
            image = TmdbConfigData.get().getPosterUrl(from.storedShow.posterImage),
            nextEpisode = nextEpisode?.run {
                WatchingItemUiModel.NextEpisode(
                    id = this.id,
                    body = "Watch next: ",
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
