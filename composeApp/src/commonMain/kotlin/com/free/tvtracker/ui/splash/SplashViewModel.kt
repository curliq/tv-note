package com.free.tvtracker.ui.splash

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.ui.ViewModel

class SplashViewModel(
    private val localDataSource: LocalSqlDataProvider,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    fun initialDestination(): Destination {
        return if (localDataSource.getLocalPreferences().welcomeComplete) {
            val session = sessionRepository.loadSession()
            if (session) {
                Destination.Home
            } else {
                // edge case: welcome screen was seen, but session is not available for some reason
                Destination.Error
            }
        } else {
            Destination.Welcome
        }
    }

    enum class Destination { Welcome, Home, Error }
}
