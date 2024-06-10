package com.free.tvtracker.features.user.domain

import com.free.tvtracker.features.user.data.UserEntity
import com.free.tvtracker.features.user.data.UserJpaRepository
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.security.TokenService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val logger: TvtrackerLogger,
    private val userJpaRepository: UserJpaRepository,
    private val encoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val tokenService: TokenService,
    private val sessionService: SessionService,
) {

    data class AuthenticatedUser(val user: UserEntity, val token: String)

    fun getAuthenticatedUser(): UserEntity? {
        val userId = sessionService.getSessionUserId()
        return userJpaRepository.findById(userId).get()
    }

    fun getAuthenticatedUserReference(): UserEntity? {
        val userId = sessionService.getSessionUserId()
        return userJpaRepository.getReferenceById(userId)
    }

    fun getAuthenticatedUserId(): Int? {
        return sessionService.getSessionUserId()
    }

    fun createUser(email: String, password: String): AuthenticatedUser? {
        val encryptedPassword = encoder.encode(password)
        val user = UserEntity(email = email, password = encryptedPassword)
        try {
            userJpaRepository.save(user)
        } catch (e: Exception) {
            logger.get.debug("Unable to create user: ", e)
            return null
        }
        val accessToken = tokenService.generate(user) ?: return null
        return AuthenticatedUser(user = user, token = accessToken)
    }

    fun login(email: String, password: String): AuthenticatedUser? {
        try {
            // todo: not throwing error when email doesnt exist
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    email,
                    password
                )
            )
        } catch (e: Exception) {
            logger.get.debug("unable to authenticate: ", e)
            return null
        }
        val user = userJpaRepository.findByEmailIs(email)!!
        logger.get.debug("Successfully logged in user: ${user.email}")
        val accessToken = tokenService.generate(user) ?: return null
        return AuthenticatedUser(user = user, token = accessToken)
    }

    fun createBearerToken(): String? {
        val user = getAuthenticatedUser() ?: return null
        return tokenService.generate(user)
    }

    fun saveFcmToken(token: String) {
        val user = getAuthenticatedUser() ?: return
        user.fcmToken = token
        userJpaRepository.save(user)
    }
}
