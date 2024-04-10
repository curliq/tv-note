package com.free.tvtracker.tracked.domain

import com.free.tvtracker.core.tmdb.data.enums.TmdbShowStatus
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.domain.StoredShowsService
import com.free.tvtracker.tracked.data.TrackedShowEntity
import com.free.tvtracker.tracked.data.TrackedShowEpisodeEntity
import com.free.tvtracker.tracked.data.TrackedShowEpisodeJdbcRepository
import com.free.tvtracker.tracked.data.TrackedShowJdbcRepository
import com.free.tvtracker.tracked.data.TrackedShowJpaRepository
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.user.domain.UserService
import org.springframework.stereotype.Service

@Service
class WatchedShowsService(
    private val trackedShowJpaRepository: TrackedShowJpaRepository,
    private val trackedShowJdbcRepository: TrackedShowJdbcRepository,
    private val trackedShowEpisodeJdbcRepository: TrackedShowEpisodeJdbcRepository,
    private val sessionService: SessionService,
    private val userService: UserService,
    private val storedShowsService: StoredShowsService,
) {
    fun addShow(body: AddShowRequest): TrackedShowEntity? {
        val userId = userService.getAuthenticatedUserId()!!
        val storedShow = storedShowsService.createStoredShow(body.tmdbShowId)
        val trackedShow = TrackedShowEntity(
            userId = userId,
            watchlisted = body.wishlisted,
            storedShow = storedShow.first,
        )
        trackedShowJdbcRepository.save(trackedShow)
        trackedShow.attachEpisodes(storedShow.second)
        return trackedShow
    }

    private fun TrackedShowEntity.attachEpisodes(episodes: List<StoredEpisodeEntity>) {
        this.storedShow.storedEpisodes = episodes
    }

    fun addEpisode(body: AddEpisodesRequest): List<TrackedShowEpisodeEntity> {
        val trackedShow = trackedShowJpaRepository.findById(body.trackedShowId).get()
        val episodes = body.episodeIds.map {
            TrackedShowEpisodeEntity(
                storedEpisodeId = it,
                trackedTvShow = trackedShow
            )
        }
        trackedShowEpisodeJdbcRepository.saveBatch(episodes)
        return episodes
    }

    fun getOngoingShows(): List<TrackedShowEntity> {
        val userId = sessionService.getSessionUserId()
        return trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = false)
    }

    fun getWatchlistedShows(): List<TrackedShowEntity> {
        val userId = sessionService.getSessionUserId()
        return trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = true)
    }

    fun getFinishedShows(): List<TrackedShowEntity> {
        val userId = sessionService.getSessionUserId()
        return trackedShowJpaRepository.findByUserIdAndWatchlistedAndStoredShowStatusIs(
            userId,
            watchlisted = false,
            status = TmdbShowStatus.ENDED.field
        )
    }
}
