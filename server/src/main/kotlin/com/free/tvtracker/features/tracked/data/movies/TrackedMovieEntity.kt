package com.free.tvtracker.features.tracked.data.movies

import com.free.tvtracker.features.watchlists.data.WatchlistTrackedMovieEntity
import com.free.tvtracker.storage.movies.data.StoredMovieEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "tracked_movies", uniqueConstraints = [UniqueConstraint(columnNames = ["storedmovie_id", "user_id"])])
data class TrackedMovieEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at_datetime", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

    @Column(name = "user_id", nullable = false)
    val userId: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storedmovie_id", nullable = false)
    val storedMovie: StoredMovieEntity = StoredMovieEntity(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "movie")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var watchlistTrackedMovies: List<WatchlistTrackedMovieEntity> = emptyList(),

    @Column(name = "watchlisted", nullable = false)
    val watchlisted: Boolean = false
)
