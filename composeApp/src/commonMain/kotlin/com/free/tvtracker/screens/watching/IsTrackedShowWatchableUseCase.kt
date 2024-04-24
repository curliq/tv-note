package com.free.tvtracker.screens.watching

import com.free.tvtracker.tracked.response.TrackedShowApiModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

class IsTrackedShowWatchableUseCase(
    private val getNextUnwatchedEpisodeUseCase: GetNextUnwatchedEpisodeUseCase,
    private val clockNow: Instant = Clock.System.now()
) {

    private fun allEpisodesWatched(): (TrackedShowApiModel) -> Boolean {
        return { show ->
            val watchedAllEpisodes = show.watchedEpisodes.size == show.storedShow.storedEpisodes.size
            watchedAllEpisodes
        }
    }

    fun canWatch(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        return shows.filterNot(allEpisodesWatched())
    }

    fun waitingShortTerm(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        return shows.filterNot(allEpisodesWatched()).filter { show ->
            val upcoming = getNextUnwatchedEpisodeUseCase(show)
            val systemTZ = TimeZone.currentSystemDefault()
            upcoming?.airDate != null && LocalDate.parse(upcoming.airDate!!).atTime(0, 0).toInstant(systemTZ)
                .compareTo(clockNow.plus(60, DateTimeUnit.DAY, systemTZ)) == 1
        }
    }

//    fun waitingLongTerm(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
//
//    }
}
