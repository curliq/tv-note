package com.free.tvtracker.storage.movies.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoredMovieJpaRepository : JpaRepository<StoredMovieEntity, Int> {
    fun findByTmdbId(tmdbId: Int): StoredMovieEntity?
}
