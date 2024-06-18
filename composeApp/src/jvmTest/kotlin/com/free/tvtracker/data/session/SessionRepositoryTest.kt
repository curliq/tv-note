package com.free.tvtracker.data.session

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.expect.data.TvHttpClientEndpoints
import com.free.tvtracker.user.response.SessionApiModel
import com.free.tvtracker.user.response.SessionApiResponse
import com.free.tvtracker.user.response.UserApiModel
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionRepositoryTest {
    val sessionStore: SessionStore = mockk(relaxed = true)

    @Test
    fun `GIVEN no session WHEN init THEN anon session is created`() = runTest {
        val httpClient: TvHttpClientEndpoints = mockk {
            coEvery { createAnonUser() } returns SessionApiResponse(
                SessionApiModel(
                    "token1",
                    UserApiModel("", 1, "", "", true)
                )
            )
        }
        val localStore: LocalSqlDataProvider = mockk {
            every { getSession() } returns null
        }
        val sut = SessionRepository(httpClient, localStore, sessionStore)
        sut.createAnonSession()
        coVerify(exactly = 1) { httpClient.createAnonUser() }
    }

    @Test
    fun `GIVEN no session WHEN init THEN new session is kept in reference`() = runTest {
        val httpClient: TvHttpClientEndpoints = mockk {
            coEvery { createAnonUser() } returns SessionApiResponse(
                SessionApiModel(
                    "token1",
                    UserApiModel("", 1, "", "", true)
                )
            )
        }
        val localStore: LocalSqlDataProvider = mockk(relaxed = true) {
            every { getSession() } returns null
        }
        val sut = SessionRepository(httpClient, localStore, sessionStore)
        sut.createAnonSession()
        verify { sessionStore.token = "token1" }
    }

    @Test
    fun `GIVEN a session WHEN init THEN no new session is created`() = runTest {
        val httpClient: TvHttpClientEndpoints = mockk()
        val localStore: LocalSqlDataProvider = mockk {
            every { getSession() } returns SessionClientEntity("token1", "", 1, "", "", true)
        }
        val sut = SessionRepository(httpClient, localStore, sessionStore)
        sut.loadSession()
        coVerify { httpClient wasNot called }
        verify { sessionStore.token = "token1" }
    }

    @Test
    fun `GIVEN a session WHEN init THEN locally stored session is available`() = runTest {
        val httpClient: TvHttpClientEndpoints = mockk()
        val localStore: LocalSqlDataProvider = mockk {
            every { getSession() } returns SessionClientEntity("token1", "", 1, "", "", true)
        }
        val sut = SessionRepository(httpClient, localStore, sessionStore)
        sut.loadSession()
        verify { sessionStore.token = "token1" }
    }

    @Test
    fun `GIVEN new anon session is created THEN its stored`() = runTest {
        val httpClient: TvHttpClientEndpoints = mockk {
            coEvery { createAnonUser() } returns SessionApiResponse(
                SessionApiModel(
                    "token1",
                    UserApiModel("", 1, "", "", true)
                )
            )
        }
        val localStore: LocalSqlDataProvider = mockk(relaxed = true)
        val sut = SessionRepository(httpClient, localStore, sessionStore)
        sut.createAnonSession()
        coVerify(exactly = 1) { localStore.saveSession(withArg {
            assertEquals("token1", it.token)
        }) }
    }
}
