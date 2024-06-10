package com.free.tvtracker.di

import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.discover.DiscoverViewModel
import com.free.tvtracker.screens.finished.FinishedShowsViewModel
import com.free.tvtracker.screens.person.PersonViewModel
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.watching.WatchingViewModel
import com.free.tvtracker.screens.watchlist.WatchlistedShowsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelsModule : KoinComponent {
    val addTrackedViewModel: AddTrackedViewModel by inject()
    val watchingViewModel: WatchingViewModel by inject()
    val detailsViewModel: DetailsViewModel by inject()
    val finishedShowsViewModel: FinishedShowsViewModel by inject()
    val watchlistedShowsViewModel: WatchlistedShowsViewModel by inject()
    val personViewModel: PersonViewModel by inject()
    val discoverViewModel: DiscoverViewModel by inject()
}
