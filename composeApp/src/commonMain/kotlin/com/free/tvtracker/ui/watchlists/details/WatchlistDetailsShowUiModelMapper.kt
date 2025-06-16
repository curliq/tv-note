package com.free.tvtracker.ui.watchlists.details

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class WatchlistDetailsShowUiModelMapper(
    private val getShowStatusUseCase: GetShowStatusUseCase,
) : Mapper<TrackedContentApiModel, WatchlistDetailsShowUiModel> {
    override fun map(from: TrackedContentApiModel): WatchlistDetailsShowUiModel {
        return if (from.isTvShow) {
            WatchlistDetailsShowUiModel(
                tmdbId = from.tvShow!!.storedShow.tmdbId,
                title = from.tvShow!!.storedShow.title,
                image = TmdbConfigData.get().getBackdropUrl(from.tvShow!!.storedShow.backdropImage),
                status = getShowStatusUseCase(
                    from.tvShow!!.storedShow.status,
                    from.tvShow!!.storedShow.storedEpisodes.firstOrNull()?.airDate,
                    from.tvShow!!.storedShow.storedEpisodes.lastOrNull()?.airDate
                ),
                isTvShow = true
            )
        } else {
            WatchlistDetailsShowUiModel(
                tmdbId = from.movie!!.storedMovie.tmdbId,
                title = from.movie!!.storedMovie.title,
                image = TmdbConfigData.get().getBackdropUrl(from.movie!!.storedMovie.backdropImage),
                status = from.movie!!.storedMovie.releaseDate?.run {
                    LocalDate.parse(input = this).format(
                        LocalDate.Format {
                            dayOfMonth(padding = Padding.NONE); char(' ')
                            monthName(MonthNames.ENGLISH_FULL); char(' ')
                            year()
                        })
                } ?: "(no release date)",
                isTvShow = false
            )
        }
    }

}
