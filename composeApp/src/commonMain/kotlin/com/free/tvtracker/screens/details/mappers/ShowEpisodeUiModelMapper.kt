package com.free.tvtracker.screens.details.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.utils.TmdbConfigData
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

class ShowEpisodeUiModelMapper(
    private val clock: Clock = Clock.System,
) : MapperWithOptions<EpisodeApi, EpisodeUi, ShowSeasonUiModelMapper.O> {

    override fun map(from: EpisodeApi, options: ShowSeasonUiModelMapper.O): EpisodeUi {
        return EpisodeUi(
            id = from.id,
            thumbnail = TmdbConfigData.get().getStillUrl(from.thumbnail),
            number = from.number.toString(),
            name = from.name ?: "",
            releaseDate = from.airDate ?: "",
            watched = options.trackedShowApiModel?.watchedEpisodes?.firstOrNull {
                it.storedEpisodeId == from.id
            } != null,
            isWatchable = LocalDate.parse(from.airDate ?: "").atTime(0, 0) < clock.now()
                .toLocalDateTime(TimeZone.UTC),
        )
    }
}

