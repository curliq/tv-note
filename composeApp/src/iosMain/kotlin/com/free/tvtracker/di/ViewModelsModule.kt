package com.free.tvtracker.di

import com.free.tvtracker.ui.details.ContentDetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.SettingsViewModel
import com.free.tvtracker.ui.settings.login.LoginViewModel
import com.free.tvtracker.ui.settings.signup.SignupViewModel
import com.free.tvtracker.ui.splash.SplashViewModel
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsViewModel
import com.free.tvtracker.ui.watchlists.list.WatchlistsViewModel
import com.free.tvtracker.ui.welcome.WelcomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ViewModelsModule : KoinComponent {
    val splashViewModel: SplashViewModel by inject()
    val loginViewModel: LoginViewModel by inject()
    val signupViewModel: SignupViewModel by inject()
    val welcomeViewModel: WelcomeViewModel by inject()
    val addTrackedViewModel: AddTrackedViewModel by inject()
    val watchingViewModel: WatchingViewModel by inject()
    fun contentDetailsViewModel(qualifier: String): ContentDetailsViewModel {
        return get<ContentDetailsViewModel>()
    }
    fun personViewModel(qualifier: String): PersonViewModel {
        return get<PersonViewModel>()
    }
    val watchlistsViewModel: WatchlistsViewModel by inject()
    val discoverViewModel: DiscoverViewModel by inject()
    val settingsViewModel: SettingsViewModel by inject()
    val watchlistDetailsViewModel: WatchlistDetailsViewModel by inject()
}
