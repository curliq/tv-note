package com.free.tvtracker.features.watchlists.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchlistJpaRepository : JpaRepository<WatchlistEntity, Int> {
    fun findByUserId(userId: Int): List<WatchlistEntity>
}
