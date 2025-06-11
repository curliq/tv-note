package com.free.tvtracker.di

import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.SettingsViewModel
import com.free.tvtracker.ui.settings.login.LoginViewModel
import com.free.tvtracker.ui.settings.signup.SignupViewModel
import com.free.tvtracker.ui.splash.SplashViewModel
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlists.list.WatchlistsViewModel
import com.free.tvtracker.ui.welcome.WelcomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelsModule : KoinComponent {
    val splashViewModel: SplashViewModel by inject()
    val loginViewModel: LoginViewModel by inject()
    val signupViewModel: SignupViewModel by inject()
    val welcomeViewModel: WelcomeViewModel by inject()
    val addTrackedViewModel: AddTrackedViewModel by inject()
    val watchingViewModel: WatchingViewModel by inject()
    val detailsViewModel: DetailsViewModel by inject()
    val finishedShowsViewModel: FinishedShowsViewModel by inject()
    val watchlistsViewModel: WatchlistsViewModel by inject()
    val personViewModel: PersonViewModel by inject()
    val discoverViewModel: DiscoverViewModel by inject()
    val settingsViewModel: SettingsViewModel by inject()
}
