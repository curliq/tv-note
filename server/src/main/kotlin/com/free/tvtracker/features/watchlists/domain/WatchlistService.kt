package com.free.tvtracker.features.watchlists.domain

import com.free.tvtracker.features.tracked.api.toApiModel
import com.free.tvtracker.features.tracked.data.movies.TrackedMovieJpaRepository
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEpisodeJpaRepository
import com.free.tvtracker.features.tracked.data.shows.TrackedShowJpaRepository
import com.free.tvtracker.features.tracked.domain.TrackedContentService
import com.free.tvtracker.features.watchlists.api.toApiModel
import com.free.tvtracker.features.watchlists.data.WatchlistEntity
import com.free.tvtracker.features.watchlists.data.WatchlistJpaRepository
import com.free.tvtracker.features.watchlists.data.WatchlistTrackedMovieEntity
import com.free.tvtracker.features.watchlists.data.WatchlistTrackedMovieJpaRepository
import com.free.tvtracker.features.watchlists.data.WatchlistTrackedShowEntity
import com.free.tvtracker.features.watchlists.data.WatchlistTrackedShowJpaRepository
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.storage.shows.domain.StoredEpisodesService
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import com.free.tvtracker.watchlists.response.WatchlistApiModel
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

/**
 * Service to retrieve and update the user's tracked shows, episodes, and movies
 */
@Service
class WatchlistService(
    private val watchlistJpaRepository: WatchlistJpaRepository,
    private val trackedShowJpaRepository: TrackedShowJpaRepository,
    private val sessionService: SessionService,
    private val trackedMovieJpaRepository: TrackedMovieJpaRepository,
    private val watchlistTrackedMovieJpaRepository: WatchlistTrackedMovieJpaRepository,
    private val watchlistTrackedShowJpaRepository: WatchlistTrackedShowJpaRepository,
    private val logger: TvtrackerLogger,
    private val storedEpisodesService: StoredEpisodesService,
    private val trackedShowEpisodeJpaRepository: TrackedShowEpisodeJpaRepository,
) {

    fun getWatchlists(): List<WatchlistApiModel> {
        val userId = sessionService.getSessionUserId()
        val lists = watchlistJpaRepository.findByUserId(userId)
            .map { it.toApiModel() }
        return lists
    }

    fun saveWatchlist(name: String): List<WatchlistApiModel> {
        val userId = sessionService.getSessionUserId()
        val list = WatchlistEntity(userId = userId, name = name)
        watchlistJpaRepository.saveAndFlush(list)
        return getWatchlists()
    }

    fun renameWatchlist(watchlistId: Int, name: String): List<WatchlistApiModel> {
        val userId = sessionService.getSessionUserId()
        val list = watchlistJpaRepository.findById(watchlistId).getOrNull()
        if (list?.userId != userId) {
            logger.get.error("Attempting to rename watchlist without permission")
            throw Exception("Attempting to rename watchlist without permission")
        }
        watchlistJpaRepository.saveAndFlush(list.copy(name = name))
        return getWatchlists()
    }

    fun deleteWatchlist(watchlistId: Int): List<WatchlistApiModel> {
        val userId = sessionService.getSessionUserId()
        val list = watchlistJpaRepository.findById(watchlistId)
        if (list.getOrNull()?.userId != userId) {
            logger.get.error("Attempting to delete watchlist without permission")
            throw Exception("Attempting to delete watchlist without permission")
        }
        watchlistJpaRepository.deleteById(watchlistId)
        return getWatchlists()
    }

    fun getWatchlistContent(watchlistId: Int): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        val list = watchlistJpaRepository.findById(watchlistId)
        if (list.getOrNull()?.userId != userId) {
            logger.get.error("Attempting to fetch watchlist without permission")
            return emptyList()
        }
        val shows = list.get().shows.map {
            val eps = storedEpisodesService.getEpisodes(it.show.storedShow.tmdbId)
            val trackedEpisodes = trackedShowEpisodeJpaRepository.findAllByTrackedTvShowId(it.show.id)
            val watchlists = watchlistTrackedShowJpaRepository.findAllByShow_Id(it.show.id)
            Pair(it.show.toApiModel(eps, watchlists, watchedEpisodes = trackedEpisodes), it.createdAtDatetime)
        }
        val movies = list.get().movies.map { Pair(it.movie.toApiModel(), it.createdAtDatetime) }
        return shows.plus(movies).sortedBy { it.second }.map { it.first }
    }

    fun addTrackedContentToWatchlist(
        trackedContentId: Int,
        watchlistId: Int,
        isTvShow: Boolean
    ): TrackedContentApiModel? {
        val userId = sessionService.getSessionUserId()
        val list = watchlistJpaRepository.findById(watchlistId)
        if (list.getOrNull()?.userId != userId) {
            logger.get.error("Attempting to add to a watchlist without permission")
            return null
        }
        if (isTvShow) {
            val show = trackedShowJpaRepository.getReferenceById(trackedContentId)
            val watchlistTracked = WatchlistTrackedShowEntity(
                watchlist = watchlistJpaRepository.getReferenceById(watchlistId),
                show = show
            )
            watchlistTrackedShowJpaRepository.saveAndFlush(watchlistTracked)
            val watchlists = watchlistTrackedShowJpaRepository.findAllByShow(show)
            val eps = storedEpisodesService.getEpisodes(show.storedShow.tmdbId)
            return show.toApiModel(eps, watchlists)
        } else {
            val movie = trackedMovieJpaRepository.getReferenceById(trackedContentId)
            val watchlistTracked = WatchlistTrackedMovieEntity(
                watchlist = watchlistJpaRepository.getReferenceById(watchlistId),
                movie = movie
            )
            watchlistTrackedMovieJpaRepository.saveAndFlush(watchlistTracked)
            return movie.toApiModel()
        }
    }

    fun deleteTrackedContentFromWatchlist(
        trackedContentId: Int,
        watchlistId: Int,
        isTvShow: Boolean,
    ): TrackedContentApiModel? {
        val userId = sessionService.getSessionUserId()
        if (isTvShow) {
            val show = trackedShowJpaRepository.getReferenceById(trackedContentId)
            if (show.userId != userId) {
                logger.get.error("Attempting to delete show from a watchlist without permission")
                return null
            }
            logger.get.debug("Deleting watchlist with id: $trackedContentId, watchlistId: $watchlistId")
            watchlistTrackedShowJpaRepository.deleteByShowIdAndWatchlistId(trackedContentId, watchlistId)
            return show.toApiModel()
        } else {
            val movie = trackedMovieJpaRepository.getReferenceById(trackedContentId)
            if (movie.userId != userId) {
                logger.get.error("Attempting to delete movie from a watchlist without permission")
                return null
            }
            watchlistTrackedMovieJpaRepository.deleteByMovieIdAndWatchlistId(trackedContentId, watchlistId)
            return movie.toApiModel()
        }
    }
}
