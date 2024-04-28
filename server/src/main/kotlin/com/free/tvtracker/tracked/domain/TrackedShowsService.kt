package com.free.tvtracker.tracked.domain

import com.free.tvtracker.domain.GetNextUnwatchedEpisodeUseCase
import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.stored.shows.domain.StoredShowsService
import com.free.tvtracker.tracked.api.toApiModel
import com.free.tvtracker.tracked.data.TrackedShowEntity
import com.free.tvtracker.tracked.data.TrackedShowEpisodeEntity
import com.free.tvtracker.tracked.data.TrackedShowEpisodeJdbcRepository
import com.free.tvtracker.tracked.data.TrackedShowJdbcRepository
import com.free.tvtracker.tracked.data.TrackedShowJpaRepository
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.user.domain.UserService
import org.springframework.stereotype.Service

@Service
class TrackedShowsService(
    private val trackedShowJpaRepository: TrackedShowJpaRepository,
    private val trackedShowJdbcRepository: TrackedShowJdbcRepository,
    private val trackedShowEpisodeJdbcRepository: TrackedShowEpisodeJdbcRepository,
    private val sessionService: SessionService,
    private val userService: UserService,
    private val storedShowsService: StoredShowsService,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase
) {
    fun addShow(body: AddShowRequest): TrackedShowEntity? {
        val userId = userService.getAuthenticatedUserId()!!
        val storedShow = storedShowsService.createOrUpdateStoredShow(body.tmdbShowId)
        val trackedShow = TrackedShowEntity(
            userId = userId,
            watchlisted = body.wishlisted,
            storedShow = storedShow.first,
        )
        trackedShowJdbcRepository.save(trackedShow)
        return trackedShow
    }

    fun addEpisode(body: AddEpisodesRequest): List<TrackedShowEpisodeEntity> {
        val episodes = body.episodes.map {
            TrackedShowEpisodeEntity(
                id = "t_${it.episodeId}",
                storedEpisodeId = it.episodeId,
                trackedTvShow = trackedShowJpaRepository.getReferenceById(it.trackedShowId)
            )
        }
        trackedShowEpisodeJdbcRepository.saveBatch(episodes)
        return episodes
    }

    fun getOngoingShows(): List<TrackedShowApiModel> {
        val userId = sessionService.getSessionUserId()
        val trackedShow = trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = false).map {
            val storedShow = storedShowsService.createOrUpdateStoredShow(it.storedShow.tmdbId)
            it.copy(storedShow = storedShow.first)
        }.map { it.toApiModel() }
        return isTrackedShowWatchableUseCase.watchable(trackedShow)
    }

    fun getFinishedShows(): List<TrackedShowApiModel> {
        val userId = sessionService.getSessionUserId()
        val shows = trackedShowJpaRepository.findByUserIdAndWatchlisted(
            userId = userId,
            watchlisted = false
        ).map { it.toApiModel() }
        return isTrackedShowWatchableUseCase.unwatchable(shows)
    }

    fun getWatchlistedShows(): List<TrackedShowApiModel> {
        val userId = sessionService.getSessionUserId()
        return trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = true)
            .map { it.toApiModel() }
    }
}

