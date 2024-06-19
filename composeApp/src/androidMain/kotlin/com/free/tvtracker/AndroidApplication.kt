package com.free.tvtracker

import android.app.Application
import android.content.Context
import com.free.tvtracker.di.appModules
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
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel
import com.free.tvtracker.ui.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AndroidApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        startKoin {
            modules(appModules())
            modules(
                module {
                    viewModel { SplashViewModel(get(), get()) }
                    viewModel { WelcomeViewModel(get(), get()) }
                    viewModel { AddTrackedViewModel(get(), get(), get()) }
                    viewModel { WatchingViewModel(get(), get(), get(), get()) }
                    viewModel { FinishedShowsViewModel(get(), get(), get(), get()) }
                    viewModel { WatchlistedShowsViewModel(get(), get(), get(), get()) }
                    viewModel { DetailsViewModel(get(), get(), get()) }
                    viewModel { PersonViewModel(get(), get()) }
                    viewModel { LoginViewModel(get(), get()) }
                    viewModel { SignupViewModel(get()) }
                    single {
                        // shared on TvTrackerTheme for all activities
                        SettingsViewModel(get(), get(), get())
                    }
                    single {
                        // why `single` and not `viewmodel`? to share it
                        // between the discover and recommendations activities
                        DiscoverViewModel(get(), get(), get(), get())
                    }
                }
            )
        }
    }
}
