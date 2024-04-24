package com.free.tvtracker.tracked.data

import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(
    name = "tracked_episodes",
    uniqueConstraints = [UniqueConstraint(columnNames = ["storedepisode_id", "trackedshow_id"])]
)
data class TrackedShowEpisodeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @CreationTimestamp
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storedepisode_id", nullable = false, insertable = false, updatable = false)
    val storedEpisode: StoredEpisodeEntity = StoredEpisodeEntity(),

    @Column(name="storedepisode_id", nullable = false)
    val storedEpisodeId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trackedshow_id", nullable = false)
    val trackedTvShow: TrackedShowEntity = TrackedShowEntity(),
)
