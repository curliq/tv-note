package com.free.tvtracker.features.tracked.domain

import com.free.tvtracker.domain.IsTrackedShowWatchableUseCase
import com.free.tvtracker.features.search.SearchService
import com.free.tvtracker.features.tracked.api.toApiModel
import com.free.tvtracker.features.tracked.data.movies.TrackedMovieEntity
import com.free.tvtracker.features.tracked.data.movies.TrackedMovieJdbcRepository
import com.free.tvtracker.features.tracked.data.movies.TrackedMovieJpaRepository
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEntity
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEpisodeEntity
import com.free.tvtracker.features.tracked.data.shows.TrackedShowEpisodeJdbcRepository
import com.free.tvtracker.features.tracked.data.shows.TrackedShowJdbcRepository
import com.free.tvtracker.features.tracked.data.shows.TrackedShowJpaRepository
import com.free.tvtracker.features.watchlists.data.WatchlistTrackedShowJpaRepository
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.storage.movies.domain.StoredMoviesService
import com.free.tvtracker.storage.shows.domain.StoredEpisodesService
import com.free.tvtracker.storage.shows.domain.StoredShowsService
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody.Episode
import com.free.tvtracker.tracked.request.AddMovieApiRequestBody
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.request.SetShowWatchlistedApiRequestBody
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import org.springframework.stereotype.Service

/**
 * Service to retrieve and update the user's tracked shows, episodes, and movies
 */
@Service
class TrackedContentService(
    private val logger: TvtrackerLogger,
    private val trackedShowJpaRepository: TrackedShowJpaRepository,
    private val trackedShowJdbcRepository: TrackedShowJdbcRepository,
    private val trackedShowEpisodeJdbcRepository: TrackedShowEpisodeJdbcRepository,
    private val sessionService: SessionService,
    private val storedShowsService: StoredShowsService,
    private val storedMoviesService: StoredMoviesService,
    private val trackedMovieJpaRepository: TrackedMovieJpaRepository,
    private val trackedMovieJdbcRepository: TrackedMovieJdbcRepository,
    private val searchService: SearchService,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase,
    private val storedEpisodesService: StoredEpisodesService,
    private val watchlistTrackedShowJpaRepository: WatchlistTrackedShowJpaRepository
) {
    fun addShow(body: AddShowApiRequestBody): TrackedShowEntity? {
        var storedShow = storedShowsService.getStoredShow(body.tmdbShowId)
        if (storedShow == null) {
            val showResponse = searchService.getShow(body.tmdbShowId)
            storedShow = storedShowsService.createOrUpdateStoredShow(showResponse)
        }
        var trackedShow = TrackedShowEntity(
            userId = sessionService.getSessionUserId(),
            watchlisted = body.watchlisted,
            storedShow = storedShow,
        )
        trackedShow = trackedShowJpaRepository.saveAndFlush(trackedShow)
        if (body.addAllEpisodes) {
            val trackedShowId = trackedShow.id
            val storedEpisodes = storedEpisodesService.getEpisodes(storedShow.tmdbId)
            val eps = addEpisodes(storedEpisodes.map { Episode(trackedShowId, it.id) })
            // assign episodes to tracked show just to return them in the api
            trackedShow.watchedEpisodes = eps.toSet()
        }
        return trackedShow
    }

    fun getTrackedShow(trackedShowId: Int): TrackedShowEntity? {
        return trackedShowJpaRepository.findById(trackedShowId).orElse(null)
    }

    fun addMovie(body: AddMovieApiRequestBody): TrackedMovieEntity? {
        var storedMovie = storedMoviesService.getStoredMovie(body.tmdbMovieId)
        if (storedMovie == null) {
            val showResponse = searchService.getMovie(body.tmdbMovieId)
            storedMovie = storedMoviesService.createOrUpdateStoredShow(showResponse)
        }
        val trackedMovie = TrackedMovieEntity(
            userId = sessionService.getSessionUserId(),
            watchlisted = body.watchlisted,
            storedMovie = storedMovie,
        )
        trackedMovieJpaRepository.saveAndFlush(trackedMovie)
        return trackedMovie
    }

    fun addEpisodes(episodes: List<Episode>): List<TrackedShowEpisodeEntity> {
        val episodes = episodes.map { episode ->
            val trackedTvShow = trackedShowJpaRepository.getReferenceById(episode.trackedShowId)
            TrackedShowEpisodeEntity(
                id = "t_${trackedTvShow.id}_${episode.episodeId}",
                storedEpisodeId = episode.episodeId,
                trackedTvShow = trackedTvShow
            )
        }
        trackedShowEpisodeJdbcRepository.saveBatch(episodes)
        return episodes
    }

    fun getOngoingShows(): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        val trackedShow =
            trackedShowJdbcRepository.findByUserIdAndWatchlistedWithAllRelations(userId, watchlisted = false)
                .map { it.toApiModel() }
        return isTrackedShowWatchableUseCase.watchable(trackedShow)
    }

    fun getFinishedShows(): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        logger.get.debug("TrackedContentService: get finished shows: start")
        val shows =
            trackedShowJdbcRepository.findByUserIdAndWatchlistedWithAllRelations(userId = userId, watchlisted = false)
                .map { it.toApiModel() }
        logger.get.debug("TrackedContentService: get finished shows: finish")
        val movies = trackedMovieJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = false)
            .map { it.toApiModel() }.sortedByDescending { it.movie!!.createdAtDatetime }
        return isTrackedShowWatchableUseCase.unwatchable(shows).plus(movies)
    }

    fun getWatchlistedShows(): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        logger.get.debug("TrackedContentService: get watchlisted shows: start")
        val shows = trackedShowJdbcRepository.findByUserIdAndWatchlistedWithAllRelations(userId, watchlisted = true)
            .map { it.toApiModel() }
        logger.get.debug("TrackedContentService: get watchlisted shows: finish")
        logger.get.debug("TrackedContentService: get finished movies: start")
        val movies = trackedMovieJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = true)
            .map { it.toApiModel() }
        logger.get.debug("TrackedContentService: get finished movies: finish")
        return shows.plus(movies).sortedByDescending { it.tvShow?.createdAtDatetime ?: it.movie!!.createdAtDatetime }
    }

    fun migrateShows(fromUserId: Int, toUserId: Int) {
        trackedShowJdbcRepository.batchChangeUser(fromUserId, toUserId)
        trackedMovieJdbcRepository.batchChangeUser(fromUserId, toUserId)
    }

    fun setShowWatchlistFlag(body: SetShowWatchlistedApiRequestBody): TrackedContentApiModel {
        if (body.isTvShow) {
            val show = trackedShowJpaRepository.findById(body.trackedContentId).get()
                .copy(watchlisted = body.watchlisted)
            val eps = storedEpisodesService.getEpisodes(show.storedShow.tmdbId)
            val watchlists = watchlistTrackedShowJpaRepository.findAllByShow(show)
            return trackedShowJpaRepository.saveAndFlush(show).toApiModel(eps, watchlists)
        } else {
            val movie =
                trackedMovieJpaRepository.findById(body.trackedContentId).get().copy(watchlisted = body.watchlisted)
            return trackedMovieJpaRepository.saveAndFlush(movie).toApiModel()
        }
    }

    fun delete(trackedContentId: Int, isTvShow: Boolean) {
        if (isTvShow) {
            watchlistTrackedShowJpaRepository.deleteAllByShow_Id(trackedContentId)
            trackedShowJpaRepository.deleteById(trackedContentId)
        } else {
            trackedMovieJpaRepository.deleteById(trackedContentId)
        }
    }
}
