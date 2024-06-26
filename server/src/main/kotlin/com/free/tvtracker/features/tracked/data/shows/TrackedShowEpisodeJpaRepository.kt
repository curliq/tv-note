package com.free.tvtracker.features.tracked.data.shows

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TrackedShowEpisodeJpaRepository : JpaRepository<TrackedShowEpisodeEntity, Int>
