package com.free.tvtracker.storage.shows.data

import com.free.tvtracker.features.tracked.data.shows.TrackedShowEpisodeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "stored_episodes")
data class StoredEpisodeEntity(

    @Id
    val id: Int = 0,

    @CreationTimestamp
    @Column(
        name = "created_at_datetime",
        nullable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    val createdAtDatetime: String = "",

    @UpdateTimestamp
    @Column(name = "updated_at_datetime", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val updatedAtDatetime: String? = null,

    @Column(name = "air_date", nullable = true)
    val airDate: String? = null,

    @Column(name = "episode_name", nullable = true)
    val episodeName: String? = null,

    @Column(name = "thumbnail", nullable = true)
    val thumbnail: String? = null,

    @Column(name = "season_number", nullable = false)
    val seasonNumber: Int = 0,

    @Column(name = "episode_number", nullable = false)
    val episodeNumber: Int = 0,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "storedEpisode")
    val trackedEpisodes: List<TrackedShowEpisodeEntity> = emptyList(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storedshow_id", nullable = false)
    val storedShow: StoredShowEntity = StoredShowEntity(),
)
