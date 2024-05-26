package com.free.tvtracker.storage.shows.data

import com.free.tvtracker.features.tracked.data.TrackedShowEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "stored_shows")
data class StoredShowEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at_datetime", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var createdAtDatetime: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at_datetime", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val updatedAtDatetime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "tmdb_id", unique = true, nullable = false)
    val tmdbId: Int = 0,

    @Column(nullable = false)
    val title: String = "",

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "storedShow")
    var storedEpisodes: List<StoredEpisodeEntity> = emptyList(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "storedShow")
    val trackedShows: List<TrackedShowEntity> = emptyList(),

    /**
     * See [com.free.tvtracker.tracked.response.TmdbShowStatus] for options
     */
    @Column(name = "status")
    val status: String = "",

    @Column(name = "poster_image")
    val posterImage: String = "",
)
