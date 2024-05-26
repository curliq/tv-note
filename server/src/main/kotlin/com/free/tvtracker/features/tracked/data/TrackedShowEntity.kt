package com.free.tvtracker.features.tracked.data

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

@Entity
@Table(name = "tracked_shows", uniqueConstraints = [UniqueConstraint(columnNames = ["storedshow_id", "user_id"])])
data class TrackedShowEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at_datetime", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

//    @ManyToOne(fetch = FetchType.LAZY, cascade = [(CascadeType.PERSIST)])
//    @JoinColumn(name = "user_id", updatable = false, insertable = false, nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    val user: UserEntity = UserEntity(),

    @Column(name = "user_id", nullable = false)
    val userId: Int = 0,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], mappedBy = "trackedTvShow")
    var watchedEpisodes: List<TrackedShowEpisodeEntity> = emptyList(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storedshow_id", nullable = false)
    val storedShow: StoredShowEntity = StoredShowEntity(),

    val watchlisted: Boolean = false
)
