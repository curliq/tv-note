package com.free.tvtracker.domain

import com.free.tvtracker.domain.TrackedShowFiltering.COMING_SOON_DAYS_AWAY
import com.free.tvtracker.constants.TmdbShowStatus
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
    private val systemTZ = TimeZone.currentSystemDefault()

    private fun allEpisodesWatched(): (TrackedShowApiModel) -> Boolean {
        return { show ->
            val watchedAllEpisodes = show.watchedEpisodes.size == show.storedShow.storedEpisodes.size
            watchedAllEpisodes
        }
    }

    private fun isNextEpisodeSoon(): (TrackedShowApiModel) -> Boolean {
        return { show ->
            val upcoming = getNextUnwatchedEpisodeUseCase(show)
            upcoming?.airDate != null && LocalDate.parse(upcoming.airDate).atTime(0, 0)
                .toInstant(systemTZ) < clockNow.plus(COMING_SOON_DAYS_AWAY, DateTimeUnit.DAY, systemTZ)
        }
    }

    private fun isNextEpisodeAvailable(): (TrackedShowApiModel) -> Boolean {
        return { show ->
            val upcoming = getNextUnwatchedEpisodeUseCase(show)
            upcoming?.airDate != null && LocalDate.parse(upcoming.airDate).atTime(0, 0).toInstant(systemTZ) < clockNow
        }
    }

    fun canWatchNow(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        return shows.filterNot(allEpisodesWatched()).filter(isNextEpisodeAvailable())
    }

    fun canWatchSoon(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        return shows
            .filterNot(allEpisodesWatched())
            .filterNot(isNextEpisodeAvailable())
            .filter(isNextEpisodeSoon())
    }

    /**
     * same as canWatchNow + canWatchSoon
     */
    fun watchable(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        return shows.filter {
            !allEpisodesWatched().invoke(it) && isNextEpisodeSoon().invoke(it)
        }
    }

    /**
     * goes to finished
     */
    fun unwatchable(shows: List<TrackedShowApiModel>): List<TrackedShowApiModel> {
        val order = mapOf(
            TmdbShowStatus.RETURNING.status to 1,
            TmdbShowStatus.ENDED.status to 2,
            TmdbShowStatus.CANCELED.status to 3
        )
        return shows.filter {
            allEpisodesWatched().invoke(it) || !isNextEpisodeSoon().invoke(it)
        }.sortedBy { order[it.storedShow.status] ?: 999 }
    }
}
