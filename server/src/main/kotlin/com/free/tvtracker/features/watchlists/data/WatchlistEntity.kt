package com.free.tvtracker.features.watchlists.data

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "watchlists")
data class WatchlistEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at_datetime", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

    @Column(name = "user_id", nullable = false)
    val userId: Int = 0,

    @Column(name = "name", nullable = false)
    val name: String = "",

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "watchlist")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var shows: List<WatchlistTrackedShowEntity> = emptyList(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "watchlist")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var movies: List<WatchlistTrackedMovieEntity> = emptyList()
)
