package com.free.tvtracker.ui.watching

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class WatchingShowUiModelMapper(
    private val getNextUnwatchedEpisodeUseCase: GetNextUnwatchedEpisodeUseCase,
) : Mapper<TrackedContentApiModel, WatchingItemUiModel> {
    override fun map(from: TrackedContentApiModel): WatchingItemUiModel {
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
            trackedShowId = from.tvShow!!.id,
            tmdbId = from.tvShow!!.storedShow.tmdbId,
            title = from.tvShow!!.storedShow.title,
            image = TmdbConfigData.get().getPosterUrl(from.tvShow!!.storedShow.posterImage),
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
