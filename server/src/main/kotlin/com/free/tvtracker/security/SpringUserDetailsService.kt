package com.free.tvtracker.security

import com.free.tvtracker.features.user.data.UserEntity
import com.free.tvtracker.features.user.data.UserJpaRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SpringUserDetailsService(
    private val userJpaRepository: UserJpaRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? =
        userJpaRepository.findByUsernameIs(username)?.mapToUserDetails()

    private fun UserEntity.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.username)
            .password(this.password)
            .roles(this.role.key)
            .build()
}
