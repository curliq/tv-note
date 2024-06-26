package com.free.tvtracker.features.tracked.data.shows

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrackedShowJpaRepository : JpaRepository<TrackedShowEntity, Int> {

    fun findByUserIdAndWatchlisted(userId: Int, watchlisted: Boolean): List<TrackedShowEntity>
}
