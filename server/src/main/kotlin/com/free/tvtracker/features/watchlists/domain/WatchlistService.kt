package com.free.tvtracker.features.watchlists.domain

import com.free.tvtracker.features.tracked.api.toApiModel
import com.free.tvtracker.features.tracked.data.movies.TrackedMovieJpaRepository
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
    private val trackedContentService: TrackedContentService,
    private val logger: TvtrackerLogger,
) {

    fun getWatchlists(): List<WatchlistApiModel> {
        val userId = sessionService.getSessionUserId()
        val lists = watchlistJpaRepository.findByUserId(userId)
            .map { it.toApiModel() }
        return getDefaultWatchlists().plus(lists)
    }

    private fun getDefaultWatchlists(): List<WatchlistApiModel> {
        val watchlisted = trackedContentService.getWatchlistedShows()
        val finished = trackedContentService.getFinishedShows()
        return listOf(
            WatchlistApiModel(
                id = WatchlistApiModel.WATCHLIST_LIST_ID,
                name = "Watchlist",
                showsCount = watchlisted.filter { it.isTvShow }.size,
                moviesCount = watchlisted.filter { !it.isTvShow }.size
            ),
            WatchlistApiModel(
                id = WatchlistApiModel.FINISHED_LIST_ID,
                name = "Finished",
                showsCount = finished.filter { it.isTvShow }.size,
                moviesCount = finished.filter { !it.isTvShow }.size
            ),
        )
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

    fun deleteWatchlist(watchlistId: Int): Boolean {
        val userId = sessionService.getSessionUserId()
        val list = watchlistJpaRepository.findById(watchlistId)
        if (list.getOrNull()?.userId != userId) {
            logger.get.error("Attempting to delete watchlist without permission")
            return false
        } else {
            watchlistJpaRepository.deleteById(watchlistId)
            return true
        }
    }

    fun getWatchlistContent(watchlistId: Int): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        val list = watchlistJpaRepository.findById(watchlistId)
        if (list.getOrNull()?.userId != userId) {
            logger.get.error("Attempting to fetch watchlist without permission")
            return emptyList()
        }
        val shows = list.get().shows.map { Pair(it.show.toApiModel(), it.createdAtDatetime) }
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
            return show.toApiModel()
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
    ): Boolean {
        val userId = sessionService.getSessionUserId()
        if (isTvShow) {
            val show = trackedShowJpaRepository.getReferenceById(trackedContentId)
            if (show.userId != userId) {
                logger.get.error("Attempting to delete show from a watchlist without permission")
                return false
            }
            logger.get.debug("Deleting watchlist with id: $trackedContentId, watchlistId: $watchlistId")
            watchlistTrackedShowJpaRepository.deleteByShowIdAndWatchlistId(trackedContentId, watchlistId)
            return true
        } else {
            val movie = trackedMovieJpaRepository.getReferenceById(trackedContentId)
            if (movie.userId != userId) {
                logger.get.error("Attempting to delete movie from a watchlist without permission")
                return false
            }
            watchlistTrackedMovieJpaRepository.deleteByMovieIdAndWatchlistId(trackedContentId, watchlistId)
            return true
        }
    }
}
