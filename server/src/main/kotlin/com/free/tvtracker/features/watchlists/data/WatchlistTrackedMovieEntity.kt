package com.free.tvtracker.features.watchlists.data

import com.free.tvtracker.features.tracked.data.movies.TrackedMovieEntity
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

/**
 * Join table for a movie added to a watchlist
 */
@Table(
    name = "watchlist_tracked_movies",
    uniqueConstraints = [UniqueConstraint(columnNames = ["tracked_movie_id", "watchlist_id"])]
)
data class WatchlistTrackedMovieEntity(
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
    @JoinColumn(name = "tracked_movie_id", nullable = false)
    val movie: TrackedMovieEntity = TrackedMovieEntity()
)
