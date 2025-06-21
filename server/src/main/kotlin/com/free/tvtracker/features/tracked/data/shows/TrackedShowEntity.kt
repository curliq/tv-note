package com.free.tvtracker.features.tracked.data.shows

import com.free.tvtracker.features.watchlists.data.WatchlistTrackedShowEntity
import com.free.tvtracker.storage.shows.data.StoredShowEntity
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
@Table(name = "tracked_shows", uniqueConstraints = [UniqueConstraint(columnNames = ["storedshow_id", "user_id"])])
data class TrackedShowEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at_datetime", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

    @Column(name = "user_id", nullable = false)
    val userId: Int = 0,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "trackedTvShow")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var watchedEpisodes: Set<TrackedShowEpisodeEntity> = emptySet(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "show")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var watchlistTrackedShows: Set<WatchlistTrackedShowEntity> = emptySet(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storedshow_id", nullable = false)
    val storedShow: StoredShowEntity = StoredShowEntity(),

    @Column(name = "watchlisted", nullable = false)
    val watchlisted: Boolean = false
)
