package com.free.tvtracker

import androidx.compose.ui.window.ComposeUIViewController
import com.free.tvtracker.ui.IosDiscoverScreen
import com.free.tvtracker.ui.details.DetailsScreen
import com.free.tvtracker.ui.details.DetailsScreenNavAction
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverScreenNavActions
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.finished.FinishedScreen
import com.free.tvtracker.ui.finished.FinishedScreenNavAction
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.search.AddTrackedScreen
import com.free.tvtracker.ui.search.AddTrackedScreenNavAction
import com.free.tvtracker.ui.search.AddTrackedScreenOriginScreen
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.SettingsScreen
import com.free.tvtracker.ui.watching.WatchingScreenNavAction
import com.free.tvtracker.ui.watching.WatchingScreen
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlist.WatchlistScreen
import com.free.tvtracker.ui.watchlist.WatchlistScreenNavAction
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel

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
