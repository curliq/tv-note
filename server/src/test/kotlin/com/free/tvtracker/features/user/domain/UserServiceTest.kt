package com.free.tvtracker.features.user.domain

import com.free.tvtracker.features.tracked.domain.TrackedShowsService
import com.free.tvtracker.features.user.data.UserEntity
import io.mockk.mockk
import kotlin.test.Test
import com.free.tvtracker.features.user.data.UserJpaRepository
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.security.TokenService
import com.free.tvtracker.user.request.LoginApiRequestBody
import com.free.tvtracker.user.request.SignupApiRequestBody
import io.mockk.every
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserServiceTest {
    private val logger: TvtrackerLogger = mockk(relaxed = true)
    private val userJpaRepository: UserJpaRepository = mockk(relaxed = true)
    private val encoder: PasswordEncoder = mockk(relaxed = true)
    private val authManager: AuthenticationManager = mockk(relaxed = true)
    private val tokenService: TokenService = mockk(relaxed = true)
    private val sessionService: SessionService = mockk(relaxed = true)
    private val trackedShowsService: TrackedShowsService = mockk(relaxed = true)
    private val sut =
        UserService(logger, userJpaRepository, encoder, authManager, tokenService, sessionService, trackedShowsService)

    @Test
    fun `GIVEN anon user WHEN create new user THEN same user is updated with credentials`() {
        every { sessionService.getSessionUserId() } returns 1
        sut.setUserCredentials(SignupApiRequestBody(username = "miguel", password = "secret"))
        verify {
            userJpaRepository.save(withArg {
                assertTrue(it.id == 1)
                assertTrue(it.username == "miguel")
            })
        }
    }

    @Test
    fun `GIVEN anon user WHEN login THEN anon user is deleted`() {
        every { sessionService.getSessionUserId() } returns 1
        sut.login(LoginApiRequestBody(username = "miguel", password = "secret"))
        verify {
            userJpaRepository.deleteById(withArg {
                assertTrue(it == 1)
            })
        }
    }

    @Test
    fun `GIVEN anon user WHEN login THEN existing user ID returned`() {
        every { sessionService.getSessionUserId() } returns 1
        every { userJpaRepository.findByUsernameIs("miguel") } returns UserEntity(id = 10)
        val res = sut.login(LoginApiRequestBody(username = "miguel", password = "secret"))
        assertNotNull(res)
        assertEquals(10, res.user.id)
    }

    @Test
    fun `GIVEN anon user WHEN login THEN tracked shows migrated from anon ID to existing user ID`() {
        every { sessionService.getSessionUserId() } returns 1
        sut.login(LoginApiRequestBody(username = "miguel", password = "secret"))
        verify {
            trackedShowsService.migrateShows(1, 10)
        }
    }

    @Test
    fun `GIVEN anon user WHEN login THEN fcm token moved from anon ID to existing user ID`() {
        every { sessionService.getSessionUserId() } returns 1
        every { userJpaRepository.save(any())} returns UserEntity() // needed for some weird mockk reason
        every { userJpaRepository.findByIdOrNull(1) } returns UserEntity(id = 1, fcmToken = "123")
        every { userJpaRepository.findByUsernameIs("miguel") } returns UserEntity(id = 10, fcmToken = null)
        sut.login(LoginApiRequestBody(username = "miguel", password = "secret"))
        verify {
            userJpaRepository.save(withArg {
                assertTrue(it.id == 10)
                assertTrue(it.fcmToken == "123")
            })
        }
    }
}
