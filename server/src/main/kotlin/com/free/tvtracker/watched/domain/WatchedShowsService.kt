package com.free.tvtracker.watched.domain

import com.free.tvtracker.core.tmdb.data.enums.TmdbShowStatus
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.domain.StoredShowsService
import com.free.tvtracker.user.domain.UserService
import com.free.tvtracker.watched.api.WatchedShowsController
import com.free.tvtracker.watched.data.TrackedShowEntity
import com.free.tvtracker.watched.data.TrackedShowEpisodeEntity
import com.free.tvtracker.watched.data.TrackedShowEpisodeJdbcRepository
import com.free.tvtracker.watched.data.TrackedShowJdbcRepository
import com.free.tvtracker.watched.data.TrackedShowJpaRepository
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
    fun addShow(body: WatchedShowsController.AddShowRequest): TrackedShowEntity? {
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

    fun addEpisode(watchedShowId: Int, body: WatchedShowsController.AddEpisodeRequest): TrackedShowEntity {
        val trackedShow = trackedShowJpaRepository.findById(watchedShowId).get()
        val episodes = body.episodeIds.map {
            TrackedShowEpisodeEntity(
                storedEpisodeId = it,
                trackedTvShow = trackedShow
            )
        }
        trackedShowEpisodeJdbcRepository.saveBatch(episodes)
        return trackedShow
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
