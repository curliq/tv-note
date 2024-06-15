package com.free.tvtracker.di

import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel
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
