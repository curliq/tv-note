package com.free.tvtracker.storage.movies.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "stored_movies")
data class StoredMovieEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @CreationTimestamp
    @Column(
        name = "created_at_datetime",
        nullable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    var createdAtDatetime: String = "",

    @UpdateTimestamp
    @Column(name = "updated_at_datetime", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val updatedAtDatetime: String? = null,

    @Column(name = "release_date", nullable = true)
    val releaseDate: String? = null,

    @Column(name = "tmdb_id", unique = true, nullable = false)
    val tmdbId: Int = 0,

    @Column(nullable = false)
    val title: String = "",

    @Column(name = "poster_image")
    val posterImage: String = "",

    @Column(name = "backdrop_image")
    val backdropImage: String = "",
)
