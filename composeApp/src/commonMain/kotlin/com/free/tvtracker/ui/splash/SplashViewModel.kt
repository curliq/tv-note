package com.free.tvtracker.ui.splash

import com.free.tvtracker.data.common.sql.LocalSqlDataProvider
import com.free.tvtracker.expect.ui.ViewModel

class SplashViewModel(
    private val localDataSource: LocalSqlDataProvider,
) : ViewModel() {
    fun initialDestination(): Destination {
        return if (localDataSource.getLocalPreferencesWelcomeComplete()) {
            Destination.Home
        } else {
            Destination.Welcome
        }
    }

    enum class Destination { Welcome, Home }
}
