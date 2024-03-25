package com.free.tvtracker.stored.shows.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoredEpisodeJpaRepository : JpaRepository<StoredEpisodeEntity, Int>{
    fun findAllByStoredShowIdIs(storedShowId: Int): List<StoredEpisodeEntity>
}
