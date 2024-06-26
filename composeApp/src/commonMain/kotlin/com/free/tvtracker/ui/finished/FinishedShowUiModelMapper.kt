package com.free.tvtracker.ui.finished

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.domain.GetShowStatusUseCase
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.ui.common.TmdbConfigData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

class FinishedShowUiModelMapper(
    private val getShowStatusUseCase: GetShowStatusUseCase,
) : Mapper<TrackedContentApiModel, FinishedShowUiModel> {
    override fun map(from: TrackedContentApiModel): FinishedShowUiModel {
        return if (from.isTvShow) {
            FinishedShowUiModel(
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
            FinishedShowUiModel(
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
