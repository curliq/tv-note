package com.free.tvtracker.di

import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.watching.WatchingViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun startKoin() {
    startKoin {
        modules(appModules())
        modules(module {
            single { AddTrackedViewModel(get(), get()) }
            single { WatchingViewModel(get(), get(), get()) }
            single { DetailsViewModel(get()) }
        })
    }
}
