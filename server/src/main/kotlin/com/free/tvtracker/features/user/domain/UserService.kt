package com.free.tvtracker.features.user.domain

import com.free.tvtracker.features.tracked.domain.TrackedContentService
import com.free.tvtracker.features.user.data.UserEntity
import com.free.tvtracker.features.user.data.UserJpaRepository
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.security.TokenService
import com.free.tvtracker.user.request.LoginApiRequestBody
import com.free.tvtracker.user.request.SignupApiRequestBody
import com.free.tvtracker.user.request.UpdatePreferencesApiRequestBody
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * auth flow:
 * 1. app creates anon user
 * signup:
 *   1. app sends signup form
 *   2. get anon user id from token
 *   3. assign data from form to that id (username, pw, email)
 *   4. return same user object but with profile data
 *   5. tracked shows unchanged
 * login:
 *   1. app sends login form
 *   2. get anon user id from token
 *   3. get user from sent credentials if valid (username, pw)
 *   4. move tracked shows from anon user to user found from credentials
 *   5. delete anon user
 */
@Service
class UserService(
    private val logger: TvtrackerLogger,
    private val userJpaRepository: UserJpaRepository,
    private val encoder: PasswordEncoder,
    private val authManager: AuthenticationManager,
    private val tokenService: TokenService,
    private val sessionService: SessionService,
    private val trackedContentService: TrackedContentService,
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

    fun setUserCredentials(body: SignupApiRequestBody): AuthenticatedUser? {
        if (body.password.isEmpty() || body.username.isEmpty()) return null
        val anonUserId = sessionService.getSessionUserId()
        logger.get.debug("setting credentials for user: $anonUserId")
        val encryptedPassword = encoder.encode(body.password)
        val user =
            UserEntity(
                id = anonUserId,
                username = body.username,
                email = body.email,
                password = encryptedPassword,
                isAnon = false
            )
        try {
            logger.get.debug("Saving user: $user")
            userJpaRepository.save(user)
        } catch (e: Exception) {
            logger.get.debug("Unable to create user: ", e)
            return null
        }
        val accessToken = tokenService.generate(user.id, user.role) ?: return null
        return AuthenticatedUser(user = user, token = accessToken)
    }

    fun createAnonUser(): AuthenticatedUser? {
        val user = UserEntity(username = UUID.randomUUID().toString(), isAnon = true)
        try {
            userJpaRepository.save(user)
        } catch (e: Exception) {
            logger.get.debug("Unable to create user: ", e)
            return null
        }
        val accessToken = tokenService.generate(user.id, user.role) ?: return null
        return AuthenticatedUser(user = user, token = accessToken)
    }

    fun login(body: LoginApiRequestBody): AuthenticatedUser? {
        if (body.password.isEmpty() || body.username.isEmpty()) return null
        val currentAnonUserId = sessionService.getSessionUserId()
        try {
            // todo: not throwing error when username doesnt exist
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    body.username,
                    body.password
                )
            )
        } catch (e: Exception) {
            logger.get.debug("unable to authenticate: ", e)
            return null
        }
        val user = userJpaRepository.findByUsernameIs(body.username)!!
        migrateUser(currentAnonUserId, user)
        val accessToken = tokenService.generate(user.id, user.role) ?: return null
        logger.get.debug("Successfully logged in user: ${user.username}")
        return AuthenticatedUser(user = user, token = accessToken)
    }

    fun saveFcmToken(token: String) {
        val user = getAuthenticatedUser() ?: return
        user.fcmToken = token
        userJpaRepository.save(user)
    }

    private fun migrateUser(fromAnonUser: Int, toUser: UserEntity) {
        trackedContentService.migrateShows(fromAnonUser, toUser.id)
        val fcmToken = userJpaRepository.findByIdOrNull(fromAnonUser)?.fcmToken
        fcmToken?.let {
            toUser.fcmToken = fcmToken
            userJpaRepository.save(toUser)
        }
        userJpaRepository.deleteById(fromAnonUser)
    }

    fun updatePreferences(body: UpdatePreferencesApiRequestBody): UserEntity? {
        val user = getAuthenticatedUser() ?: return null
        body.pushPrefsAllowed?.let {
            user.preferencesPushAllowed = it
        }
        userJpaRepository.save(user)
        return user
    }
}
