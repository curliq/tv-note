package com.free.tvtracker.stored.shows.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoredShowJpaRepository : JpaRepository<StoredShowEntity, Int> {
    fun findByTmdbId(tmdbId: Int): StoredShowEntity?
}
