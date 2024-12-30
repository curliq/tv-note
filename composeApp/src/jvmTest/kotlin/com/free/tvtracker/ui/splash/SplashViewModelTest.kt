package com.free.tvtracker.ui.splash

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.session.LocalPreferencesClientEntity
import com.free.tvtracker.data.session.SessionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class SplashViewModelTest {
    private val localStore: LocalSqlDataProvider = mockk {
        every { getLocalPreferences() } returns LocalPreferencesClientEntity(
            true,
            LocalPreferencesClientEntity.Theme.SystemDefault,
            true,
            false
        )
    }

    @Test
    fun `GIVEN has completed welcome THEN local session is fetched`() {
        val sessionRepository: SessionRepository = mockk(relaxed = true)
        val sut = SplashViewModel(localStore, sessionRepository)
        sut.initialDestination()
        verify(exactly = 1) { sessionRepository.loadSession() }
    }

    @Test
    fun `GIVEN has completed welcome AND local session is fetched THEN go home`() {
        val sessionRepository: SessionRepository = mockk {
            every { loadSession() } returns true
        }
        val sut = SplashViewModel(localStore, sessionRepository)
        assertEquals(SplashViewModel.Destination.Home, sut.initialDestination())
    }
}
