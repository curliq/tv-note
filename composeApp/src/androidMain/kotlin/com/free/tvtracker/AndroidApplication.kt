package com.free.tvtracker

import android.app.Application
import android.content.Context
import com.free.tvtracker.di.appModules
import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.discover.DiscoverViewModel
import com.free.tvtracker.screens.finished.FinishedShowsViewModel
import com.free.tvtracker.screens.person.PersonViewModel
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.watching.WatchingViewModel
import com.free.tvtracker.screens.watchlist.WatchlistedShowsViewModel
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
                    viewModel { AddTrackedViewModel(get(), get(), get()) }
                    viewModel { WatchingViewModel(get(), get(), get(), get()) }
                    viewModel { FinishedShowsViewModel(get(), get(), get(), get()) }
                    viewModel { WatchlistedShowsViewModel(get(), get(), get(), get()) }
                    viewModel { DetailsViewModel(get(), get(), get()) }
                    viewModel { PersonViewModel(get(), get()) }
                    single {
                        // why single and not viewmodel? to share it between discover and recommendations activities
                        DiscoverViewModel(get(), get(), get(), get())
                    }
                }
            )
        }
    }
}
