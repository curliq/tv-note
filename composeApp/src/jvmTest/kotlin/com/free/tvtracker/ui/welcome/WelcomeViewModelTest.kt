package com.free.tvtracker.ui.welcome

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.user.response.UserApiModel
import com.free.tvtracker.user.response.UserApiResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {
    @Test
    fun `GIVEN init THEN anon session is created`() {
        val sessionRepository: SessionRepository = mockk()
        WelcomeViewModel(mockk(), sessionRepository)
        coVerify(exactly = 1) { sessionRepository.createAnonymousSession() }
    }

    @Test
    fun `GIVEN creating session succeeds THEN proceed to home screen`() {
        val dispatcher = UnconfinedTestDispatcher()
        val sessionRepository: SessionRepository = mockk {
            coEvery { createAnonymousSession() } returns UserApiResponse.ok(UserApiModel("", 1, null, null))
        }
        val sut = WelcomeViewModel(mockk(), sessionRepository, dispatcher)
        assertEquals(WelcomeViewModel.Status.GoToHome, sut.status.value)
    }

    @Test
    fun `GIVEN creating session fails THEN stay in welcome screen`() {
        val dispatcher = UnconfinedTestDispatcher()
        val sessionRepository: SessionRepository = mockk {
            coEvery { createAnonymousSession() } returns UserApiResponse.error(ApiError.Unknown)
        }
        val sut = WelcomeViewModel(mockk(), sessionRepository, dispatcher)
        assertEquals(WelcomeViewModel.Status.InitialisationError, sut.status.value)
    }
}
