package com.free.tvtracker

import androidx.compose.ui.window.ComposeUIViewController
import com.free.tvtracker.screens.details.DetailsScreen
import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.discover.DiscoverScreen
import com.free.tvtracker.screens.finished.FinishedScreen
import com.free.tvtracker.screens.search.AddTrackedScreen
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.settings.SettingsScreen
import com.free.tvtracker.screens.watching.NavAction
import com.free.tvtracker.screens.watching.WatchingScreen
import com.free.tvtracker.screens.watching.WatchingViewModel
import com.free.tvtracker.screens.watchlist.WatchlistScreen

fun WatchingScreenViewController(navigate: (NavAction) -> Unit, watchingViewModel: WatchingViewModel) =
    ComposeUIViewController {
        WatchingScreen(navigate, watchingViewModel)
    }

fun FinishedScreenViewController() = ComposeUIViewController {
    FinishedScreen()
}

fun WatchlistScreenViewController() = ComposeUIViewController {
    WatchlistScreen()
}

fun DiscoverScreenViewController() = ComposeUIViewController {
    DiscoverScreen()
}

fun SettingsScreenViewController() = ComposeUIViewController {
    SettingsScreen()
}

fun ShowDetailsScreenViewController(detailsViewModel: DetailsViewModel, showId:Int) = ComposeUIViewController {
    DetailsScreen(detailsViewModel, showId)
}

fun AddTrackedScreenViewController(addTrackedViewModel: AddTrackedViewModel) = ComposeUIViewController {
    AddTrackedScreen(addTrackedViewModel)
}
