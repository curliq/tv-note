package com.free.tvtracker.ui.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

class ShowEpisodeUiModelMapper(
    private val clock: Clock = Clock.System,
) : MapperWithOptions<EpisodeApi, EpisodeUi, ShowSeasonUiModelMapper.O> {

    override fun map(from: EpisodeApi, options: ShowSeasonUiModelMapper.O): EpisodeUi {
        val airDate = try {
            from.airDate?.run {
                LocalDate.parse(input = from.airDate!!).format(LocalDate.Format {
                    dayOfMonth(padding = Padding.NONE); char(' ')
                    monthName(MonthNames.ENGLISH_FULL); char(' ')
                    year()
                })
            }
        } catch (e: IllegalArgumentException) {
            null
        }
        val isWatchable = try {
            LocalDate.parse(from.airDate ?: "").atTime(0, 0) < clock.now()
                .toLocalDateTime(TimeZone.UTC)
        } catch (e: IllegalArgumentException) {
            false
        }
        return EpisodeUi(
            id = from.id,
            thumbnail = TmdbConfigData.get().getStillUrl(from.thumbnail),
            number = from.number.toString(),
            name = from.name ?: "",
            releaseDate = airDate ?: "",
            watched = options.trackedShowApiModel?.watchedEpisodes?.firstOrNull {
                it.storedEpisodeId == from.id
            } != null,
            isWatchable = isWatchable,
        )
    }
}

