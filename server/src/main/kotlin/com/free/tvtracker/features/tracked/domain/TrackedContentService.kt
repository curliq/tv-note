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
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.storage.movies.domain.StoredMoviesService
import com.free.tvtracker.storage.shows.domain.StoredShowsService
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody.Episode
import com.free.tvtracker.tracked.request.AddMovieApiRequestBody
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.request.SetShowWatchlistedApiRequestBody
import com.free.tvtracker.tracked.response.TrackedContentApiModel
import org.springframework.stereotype.Service

@Service
class TrackedContentService(
    private val trackedShowJpaRepository: TrackedShowJpaRepository,
    private val trackedShowJdbcRepository: TrackedShowJdbcRepository,
    private val trackedShowEpisodeJdbcRepository: TrackedShowEpisodeJdbcRepository,
    private val sessionService: SessionService,
    private val storedShowsService: StoredShowsService,
    private val storedMoviesService: StoredMoviesService,
    private val trackedMovieJpaRepository: TrackedMovieJpaRepository,
    private val trackedMovieJdbcRepository: TrackedMovieJdbcRepository,
    private val searchService: SearchService,
    private val isTrackedShowWatchableUseCase: IsTrackedShowWatchableUseCase
) {
    fun addShow(body: AddShowApiRequestBody): TrackedShowEntity? {
        var storedShow = storedShowsService.getStoredShow(body.tmdbShowId)
        if (storedShow == null) {
            val showResponse = searchService.getShow(body.tmdbShowId)
            storedShow = storedShowsService.createOrUpdateStoredShow(showResponse)
        }
        val trackedShow = TrackedShowEntity(
            userId = sessionService.getSessionUserId(),
            watchlisted = body.watchlisted,
            storedShow = storedShow,
        )
        trackedShowJpaRepository.saveAndFlush(trackedShow)
        if (body.addAllEpisodes) {
            val trackedShowId = trackedShow.id
            val eps = addEpisode(storedShow.storedEpisodes.map { Episode(trackedShowId, it.id) })
            // assign episodes to tracked show just to return them in the api
            trackedShow.watchedEpisodes = eps
        }
        return trackedShow
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

    fun addEpisode(episodes: List<Episode>): List<TrackedShowEpisodeEntity> {
        val episodes = episodes.map {
            val trackedTvShow = trackedShowJpaRepository.getReferenceById(it.trackedShowId)
            TrackedShowEpisodeEntity(
                id = "t_${trackedTvShow.id}_${it.episodeId}",
                storedEpisodeId = it.episodeId,
                trackedTvShow = trackedTvShow
            )
        }
        trackedShowEpisodeJdbcRepository.saveBatch(episodes)
        return episodes
    }

    fun getOngoingShows(): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        val trackedShow = trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = false)
            .map { it.toApiModel() }
        return isTrackedShowWatchableUseCase.watchable(trackedShow)
    }

    fun getFinishedShows(): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        val shows = trackedShowJpaRepository.findByUserIdAndWatchlisted(userId = userId, watchlisted = false)
            .map { it.toApiModel() }
        val movies = trackedMovieJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = false)
            .map { it.toApiModel() }.sortedByDescending { it.movie!!.createdAtDatetime }
        return isTrackedShowWatchableUseCase.unwatchable(shows).plus(movies)
    }

    fun getWatchlistedShows(): List<TrackedContentApiModel> {
        val userId = sessionService.getSessionUserId()
        val shows = trackedShowJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = true)
            .map { it.toApiModel() }
        val movies = trackedMovieJpaRepository.findByUserIdAndWatchlisted(userId, watchlisted = true)
            .map { it.toApiModel() }
        return shows.plus(movies).sortedByDescending { it.tvShow?.createdAtDatetime ?: it.movie!!.createdAtDatetime }
    }

    fun migrateShows(fromUserId: Int, toUserId: Int) {
        trackedShowJdbcRepository.batchChangeUser(fromUserId, toUserId)
        trackedMovieJdbcRepository.batchChangeUser(fromUserId, toUserId)
    }

    fun setShowWatchlistFlag(body: SetShowWatchlistedApiRequestBody): TrackedContentApiModel {
        if (body.isTvShow) {
            val show =
                trackedShowJpaRepository.findById(body.trackedContentId).get().copy(watchlisted = body.watchlisted)
            return trackedShowJpaRepository.saveAndFlush(show).toApiModel()
        } else {
            val movie =
                trackedMovieJpaRepository.findById(body.trackedContentId).get().copy(watchlisted = body.watchlisted)
            return trackedMovieJpaRepository.saveAndFlush(movie).toApiModel()
        }
    }

    fun delete(trackedContentId: Int, isTvShow: Boolean) {
        if (isTvShow) {
            trackedShowJpaRepository.deleteById(trackedContentId)
        } else {
            trackedMovieJpaRepository.deleteById(trackedContentId)
        }
    }
}
