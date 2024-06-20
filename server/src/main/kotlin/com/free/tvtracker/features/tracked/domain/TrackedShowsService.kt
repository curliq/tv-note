package com.free.tvtracker.features.tracked.domain

import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.features.search.SearchService
import com.free.tvtracker.features.tracked.api.toApiModel
import com.free.tvtracker.features.tracked.data.TrackedShowEntity
import com.free.tvtracker.features.tracked.data.TrackedShowEpisodeEntity
import com.free.tvtracker.features.tracked.data.TrackedShowEpisodeJdbcRepository
import com.free.tvtracker.features.tracked.data.TrackedShowJdbcRepository
import com.free.tvtracker.features.tracked.data.TrackedShowJpaRepository
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.storage.shows.domain.StoredShowsService
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import org.springframework.stereotype.Service

@Service
class TrackedShowsService(
    private val trackedShowJpaRepository: TrackedShowJpaRepository,
    private val trackedShowJdbcRepository: TrackedShowJdbcRepository,
    private val trackedShowEpisodeJdbcRepository: TrackedShowEpisodeJdbcRepository,
    private val sessionService: SessionService,
    private val storedShowsService: StoredShowsService,
    private val searchService: SearchService,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase
) {
    fun addShow(body: AddShowApiRequestBody): TrackedShowEntity? {
        val userId = sessionService.getSessionUserId()
        var storedShow = storedShowsService.getStoredShow(body.tmdbShowId)
        if (storedShow == null) {
            val showResponse = searchService.getShow(body.tmdbShowId)
            storedShow = storedShowsService.createOrUpdateStoredShow(showResponse)
        }
        val trackedShow = TrackedShowEntity(
            userId = userId,
            watchlisted = body.watchlisted,
            storedShow = storedShow,
        )
        trackedShowJpaRepository.saveAndFlush(trackedShow)
        return trackedShow
    }

    fun addEpisode(body: AddEpisodesApiRequestBody): List<TrackedShowEpisodeEntity> {
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
        val trackedShow = trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = false)
            .map { it.toApiModel() }
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

    fun migrateShows(fromUserId: Int, toUserId: Int) {
        trackedShowJdbcRepository.batchChangeUser(fromUserId, toUserId)
    }

    fun setShowWatchlistFlag(trackedShowId: Int, watchlisted: Boolean): TrackedShowApiModel {
        val show = trackedShowJpaRepository.findById(trackedShowId).get().copy(watchlisted = watchlisted)
        return trackedShowJpaRepository.saveAndFlush(show).toApiModel()
    }
}

