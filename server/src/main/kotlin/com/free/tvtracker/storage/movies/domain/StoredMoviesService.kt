package com.free.tvtracker.storage.movies.domain

import com.free.tvtracker.storage.movies.data.StoredMovieEntity
import com.free.tvtracker.storage.movies.data.StoredMovieJpaRepository
import com.free.tvtracker.tmdb.data.TmdbMovieBigResponse
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant

@Service
class StoredMoviesService(
    private val storedMovieJpaRepository: StoredMovieJpaRepository
) {

    fun getStoredMovie(tmdbMovieId: Int): StoredMovieEntity? {
        val show = storedMovieJpaRepository.findByTmdbId(tmdbMovieId)
        if (isStoredMovieCacheValid(show)) {
            return show
        }
        return null
    }

    fun createOrUpdateStoredShow(tmdbShowResponse: TmdbMovieBigResponse): StoredMovieEntity {
        val storedMovie = storedMovieJpaRepository.findByTmdbId(tmdbId = tmdbShowResponse.id)
        val newStoredShow = buildStoredMovie(tmdbShowResponse)
        if (storedMovie != null) {
            newStoredShow.createdAtDatetime = storedMovie.createdAtDatetime
            newStoredShow.id = storedMovie.id
        }
        storedMovieJpaRepository.save(newStoredShow)
        return newStoredShow
    }

    private fun isStoredMovieCacheValid(storedMovie: StoredMovieEntity?): Boolean {
        if (storedMovie == null) {
            return false
        }
        val storedShowValidDuration = Duration.ofHours(24)
        return Timestamp.valueOf(storedMovie.updatedAtDatetime).toInstant()
            .plus(storedShowValidDuration)
            .isAfter(Instant.now())
    }

    private fun buildStoredMovie(movie: TmdbMovieBigResponse): StoredMovieEntity {
        return StoredMovieEntity(
            tmdbId = movie.id,
            title = movie.title!!,
            releaseDate = movie.releaseDate,
            posterImage = movie.posterPath ?: "",
            backdropImage = movie.backdropPath ?: "",
        )
    }
}
