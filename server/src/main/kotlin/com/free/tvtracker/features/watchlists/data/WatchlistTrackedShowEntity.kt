package com.free.tvtracker.features.watchlists.data

import com.free.tvtracker.features.tracked.data.shows.TrackedShowEntity
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

/**
 * Join table for a show added to a watchlist
 */
@Entity
@Table(
    name = "watchlist_tracked_shows",
    uniqueConstraints = [UniqueConstraint(columnNames = ["tracked_show_id", "watchlist_id"])]
)
data class WatchlistTrackedShowEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at_datetime", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watchlist_id", nullable = false)
    val watchlist: WatchlistEntity = WatchlistEntity(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracked_show_id", nullable = false)
    val show: TrackedShowEntity = TrackedShowEntity()
)
