package com.free.tvtracker.features.tracked.data.shows

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface TrackedShowEpisodeJpaRepository : JpaRepository<TrackedShowEpisodeEntity, Int> {
    @Transactional
    fun findAllByTrackedTvShowId(trackedTvShowId: Int): MutableList<TrackedShowEpisodeEntity>
}
