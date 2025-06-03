package com.free.tvtracker.features.watchlists.data

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchlistTrackedShowJpaRepository : JpaRepository<WatchlistTrackedShowEntity, Int> {

    @Transactional //this prevents org.springframework.dao.InvalidDataAccessApiUsageException
    fun deleteByShowIdAndWatchlistId(trackedContentId: Int, watchlistId: Int): Int
}
