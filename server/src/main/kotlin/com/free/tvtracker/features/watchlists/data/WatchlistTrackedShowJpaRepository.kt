package com.free.tvtracker.features.watchlists.data

import com.free.tvtracker.features.tracked.data.shows.TrackedShowEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.List

@Repository
interface WatchlistTrackedShowJpaRepository : JpaRepository<WatchlistTrackedShowEntity, Int> {

    @Transactional //this prevents org.springframework.dao.InvalidDataAccessApiUsageException
    fun deleteByShowIdAndWatchlistId(trackedContentId: Int, watchlistId: Int): Int

    @Transactional
    fun findAllByShow(show: TrackedShowEntity): MutableList<WatchlistTrackedShowEntity>

    @Transactional
    fun findAllByShow_Id(showId: Int): MutableList<WatchlistTrackedShowEntity>

    @Transactional
    fun deleteAllByShow_Id(showId: Int): Int
}
