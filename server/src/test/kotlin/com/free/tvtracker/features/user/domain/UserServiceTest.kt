package com.free.tvtracker.features.user.domain

import com.free.tvtracker.features.tracked.domain.TrackedShowsService
import com.free.tvtracker.features.user.data.UserEntity
import io.mockk.mockk
import kotlin.test.Test
import com.free.tvtracker.features.user.data.UserJpaRepository
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.security.SessionService
import com.free.tvtracker.security.TokenService
import io.mockk.every
import io.mockk.verify
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
    private val sessionService: SessionService = mockk()
    private val trackedShowsService: TrackedShowsService = mockk(relaxed = true)
    private val sut =
        UserService(logger, userJpaRepository, encoder, authManager, tokenService, sessionService, trackedShowsService)

    @Test
    fun `GIVEN anon user WHEN create new user THEN same user is updated with credentials`() {
        sut.setUserCredentials(UserController.SignupRequest(anonUserId = 1, username = "miguel", password = "secret"))
        verify {
            userJpaRepository.save(withArg {
                assertTrue(it.id == 1)
                assertTrue(it.username == "miguel")
            })
        }
    }

    @Test
    fun `GIVEN anon user WHEN login THEN anon user is deleted`() {
        sut.login(UserController.LoginRequest(anonUserId = 1, username = "miguel", password = "secret"))
        verify {
            userJpaRepository.deleteById(withArg {
                assertTrue(it == 1)
            })
        }
    }

    @Test
    fun `GIVEN anon user WHEN login THEN existing user ID returned`() {
        every { userJpaRepository.findByUsernameIs("miguel") } returns UserEntity(id = 10, username = "pedro")
        val res = sut.login(UserController.LoginRequest(anonUserId = 1, username = "miguel", password = "secret"))
        assertNotNull(res)
        assertEquals(10, res.user.id)
        assertEquals("pedro", res.user.username)
    }

    @Test
    fun `GIVEN anon user WHEN login THEN tracked shows migrated from anon ID to existing user ID`() {
        every { userJpaRepository.findByUsernameIs("miguel") } returns UserEntity(id = 10, username = "pedro")
        sut.login(UserController.LoginRequest(anonUserId = 1, username = "miguel", password = "secret"))
        verify {
            trackedShowsService.migrateShows(1, 10)
        }
    }
}
