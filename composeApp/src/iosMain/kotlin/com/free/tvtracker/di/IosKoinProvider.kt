package com.free.tvtracker.di

import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.discover.DiscoverViewModel
import com.free.tvtracker.screens.finished.FinishedShowsViewModel
import com.free.tvtracker.screens.person.PersonViewModel
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.watching.WatchingViewModel
import com.free.tvtracker.screens.watchlist.WatchlistedShowsViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun startKoin() {
    startKoin {
        modules(appModules())
        modules(module {
            single { AddTrackedViewModel(get(), get(), get()) }
            single { WatchingViewModel(get(), get(), get(), get()) }
            single { DetailsViewModel(get(), get(), get()) }
            single { FinishedShowsViewModel(get(), get(), get(), get()) }
            single { WatchlistedShowsViewModel(get(), get(), get(), get()) }
            single { PersonViewModel(get(), get()) }
            single { DiscoverViewModel(get(), get(), get(), get()) }
        })
    }
}
