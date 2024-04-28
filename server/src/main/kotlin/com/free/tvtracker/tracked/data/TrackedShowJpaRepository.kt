package com.free.tvtracker.tracked.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrackedShowJpaRepository : JpaRepository<TrackedShowEntity, Int> {

    fun findByUserIdAndWatchlisted(userId: Int, watchlisted: Boolean): List<TrackedShowEntity>
}
