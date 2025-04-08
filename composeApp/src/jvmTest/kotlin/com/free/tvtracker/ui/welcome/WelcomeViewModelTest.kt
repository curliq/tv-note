package com.free.tvtracker.ui.welcome

import com.free.tvtracker.core.Logger
import com.free.tvtracker.data.session.SessionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {
    private val sessionRepository: SessionRepository = mockk(relaxed = true)

    @Test
    fun `GIVEN init THEN anon session is created`() {
        WelcomeViewModel(
            mockk(),
            sessionRepository,
            mockk(relaxed = true),
            logger = Logger()
        )
        coVerify(exactly = 1) { sessionRepository.createAnonSession() }
    }

    @Test
    fun `GIVEN creating session succeeds THEN green light`() {
        val dispatcher = UnconfinedTestDispatcher()
        coEvery { sessionRepository.createAnonSession() } returns true
        val sut = WelcomeViewModel(mockk(), sessionRepository, mockk(relaxed = true), mockk(relaxed = true), dispatcher)
        assertEquals(WelcomeViewModel.Status.GreenLight, sut.status.value)
    }

    @Test
    fun `GIVEN creating session succeeds WHEN user taps ok THEN proceed home`() {
        val dispatcher = UnconfinedTestDispatcher()
        coEvery { sessionRepository.createAnonSession() } returns true
        val sut = WelcomeViewModel(
            mockk(relaxed = true),
            sessionRepository,
            mockk(relaxed = true),
            mockk(relaxed = true),
            dispatcher
        )
        sut.actionOk()
        assertEquals(WelcomeViewModel.Status.GoToHome, sut.status.value)
    }

    @Test
    fun `GIVEN creating session fails THEN stay in welcome screen`() {
        val dispatcher = UnconfinedTestDispatcher()
        coEvery { sessionRepository.createAnonSession() } returns false
        val sut = WelcomeViewModel(mockk(), sessionRepository, mockk(relaxed = true), mockk(relaxed = true), dispatcher)
        assertEquals(WelcomeViewModel.Status.InitialisationError, sut.status.value)
    }
}
