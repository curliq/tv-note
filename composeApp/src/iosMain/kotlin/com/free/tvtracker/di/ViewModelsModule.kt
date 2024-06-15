package com.free.tvtracker.di

import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.SettingsViewModel
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel
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
    val settingsViewModel: SettingsViewModel by inject()
}
