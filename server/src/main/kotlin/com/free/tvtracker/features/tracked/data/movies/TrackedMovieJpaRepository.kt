package com.free.tvtracker.features.tracked.data.movies

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrackedMovieJpaRepository : JpaRepository<TrackedMovieEntity, Int> {

    fun findByUserIdAndWatchlisted(userId: Int, watchlisted: Boolean): List<TrackedMovieEntity>
}
