package com.free.tvtracker.di

import com.free.tvtracker.data.iap.AppPriceProvider
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.FileExporter
import com.free.tvtracker.ui.settings.SettingsViewModel
import com.free.tvtracker.ui.settings.login.LoginViewModel
import com.free.tvtracker.ui.settings.signup.SignupViewModel
import com.free.tvtracker.ui.splash.SplashViewModel
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel
import com.free.tvtracker.ui.welcome.WelcomeViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun startKoin(appPriceProvider: AppPriceProvider, fileExporter: FileExporter) {
    startKoin {
        modules(appModules())
        modules(module {
            single { appPriceProvider }
            single { fileExporter }
            single { AddTrackedViewModel(get(), get(), get(), get(), get(), get()) }
            single { WatchingViewModel(get(), get(), get(), get(), get(), get(), get()) }
            single { FinishedShowsViewModel(get(), get(), get(), get(), get(), get(), get()) }
            single { WatchlistedShowsViewModel(get(), get(), get(), get(), get(), get(), get()) }
            single { DiscoverViewModel(get(), get(), get(), get(), get()) }
            single { SettingsViewModel(get(), get(), get(), get(), get()) }
            single { SplashViewModel(get(), get()) }
            single { WelcomeViewModel(get(), get(), get(), get()) }
            single { LoginViewModel(get(), get()) }
            single { SignupViewModel(get()) }

            // each navstack has its own state
            factory { DetailsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
            factory { PersonViewModel(get(), get()) }
        })
    }
}
