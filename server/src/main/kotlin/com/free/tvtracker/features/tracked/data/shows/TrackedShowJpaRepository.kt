package com.free.tvtracker.features.tracked.data.shows

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TrackedShowJpaRepository : JpaRepository<TrackedShowEntity, Int> {

    @EntityGraph(
        attributePaths = ["storedShow", "watchedEpisodes", "storedShow.storedEpisodes"]
    )
    fun findByUserIdAndWatchlisted(userId: Int, watchlisted: Boolean): Set<TrackedShowEntity>

    /**
     * Optimized query that fetches all necessary related data in a single query to avoid N+1 problem
     */
    @Query("""
        SELECT DISTINCT ts FROM TrackedShowEntity ts
        LEFT JOIN FETCH ts.storedShow ss
        LEFT JOIN FETCH ss.storedEpisodes se
        LEFT JOIN FETCH ts.watchedEpisodes we
        LEFT JOIN FETCH we.storedEpisode
        LEFT JOIN FETCH ts.watchlistTrackedShows wts
        LEFT JOIN FETCH wts.watchlist
        WHERE ts.userId = :userId AND ts.watchlisted = :watchlisted
    """)
    fun findByUserIdAndWatchlistedWithAllRelations(
        @Param("userId") userId: Int,
        @Param("watchlisted") watchlisted: Boolean
    ): List<TrackedShowEntity>
}
