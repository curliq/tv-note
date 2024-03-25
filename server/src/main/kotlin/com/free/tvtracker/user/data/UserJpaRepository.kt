package com.free.tvtracker.user.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Int> {
    fun findByEmailIs(email: String): UserEntity?
}
