package com.free.tvtracker.features.user.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import org.hibernate.annotations.CreationTimestamp

@Serializable
@Entity
@Table(name = "users")
class UserEntity(
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val createdAtDatetime: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(unique = true, nullable = false)
    val username: String = "",

    @Column(unique = false, nullable = true)
    val email: String? = null,

    @Column(unique = false, nullable = true)
    val password: String? = null,

    @Column
    val role: UserRole = UserRole.WATCHER,

    @Column(nullable = true)
    var fcmToken: String? = null,

    @Column(name = "preferences_push_allowed", nullable = true)
    var preferencesPushAllowed: Boolean = true,
)
