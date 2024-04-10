package com.free.tvtracker.di

import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.watching.WatchingViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelsModule : KoinComponent {
    val addTrackedViewModel: AddTrackedViewModel by inject()
    val watchingViewModel: WatchingViewModel by inject()
    val detailsViewModel: DetailsViewModel by inject()
}
