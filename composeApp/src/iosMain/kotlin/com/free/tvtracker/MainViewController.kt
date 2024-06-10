package com.free.tvtracker

import androidx.compose.ui.window.ComposeUIViewController
import com.free.tvtracker.screens.IosDiscoverScreen
import com.free.tvtracker.screens.details.DetailsScreen
import com.free.tvtracker.screens.details.DetailsScreenNavAction
import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.discover.DiscoverScreen
import com.free.tvtracker.screens.discover.DiscoverScreenNavActions
import com.free.tvtracker.screens.discover.DiscoverViewModel
import com.free.tvtracker.screens.finished.FinishedScreen
import com.free.tvtracker.screens.finished.FinishedScreenNavAction
import com.free.tvtracker.screens.finished.FinishedShowsViewModel
import com.free.tvtracker.screens.search.AddTrackedScreen
import com.free.tvtracker.screens.search.AddTrackedScreenNavAction
import com.free.tvtracker.screens.search.AddTrackedScreenOriginScreen
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.free.tvtracker.screens.settings.SettingsScreen
import com.free.tvtracker.screens.watching.WatchingScreenNavAction
import com.free.tvtracker.screens.watching.WatchingScreen
import com.free.tvtracker.screens.watching.WatchingViewModel
import com.free.tvtracker.screens.watchlist.WatchlistScreen
import com.free.tvtracker.screens.watchlist.WatchlistScreenNavAction
import com.free.tvtracker.screens.watchlist.WatchlistedShowsViewModel

fun WatchingScreenViewController(navigate: (WatchingScreenNavAction) -> Unit, viewModel: WatchingViewModel) =
    ComposeUIViewController {
        WatchingScreen(navigate, viewModel)
    }

fun FinishedScreenViewController(navigate: (FinishedScreenNavAction) -> Unit, viewModel: FinishedShowsViewModel) =
    ComposeUIViewController {
        FinishedScreen(navigate, viewModel)
    }

fun WatchlistScreenViewController(navigate: (WatchlistScreenNavAction) -> Unit, viewModel: WatchlistedShowsViewModel) =
    ComposeUIViewController {
        WatchlistScreen(viewModel, navigate)
    }

fun DiscoverScreenViewController(navigate: (DiscoverScreenNavActions) -> Unit, viewModel: DiscoverViewModel) =
    ComposeUIViewController {
        IosDiscoverScreen(viewModel, navigate)
    }

fun SettingsScreenViewController() = ComposeUIViewController {
    SettingsScreen()
}

fun ShowDetailsScreenViewController(
    detailsViewModel: DetailsViewModel,
    showId: Int,
    navigate: (DetailsScreenNavAction) -> Unit
) = ComposeUIViewController {
    DetailsScreen(detailsViewModel, showId, navigate)
}

fun AddTrackedScreenViewController(
    addTrackedViewModel: AddTrackedViewModel,
    navigate: (AddTrackedScreenNavAction) -> Unit,
    originScreen: AddTrackedScreenOriginScreen
) = ComposeUIViewController {
    AddTrackedScreen(addTrackedViewModel, navigate, originScreen)
}
