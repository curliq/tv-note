package com.free.tvtracker

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.free.tvtracker.ui.details.DetailsScreen
import com.free.tvtracker.ui.details.DetailsScreenNavAction
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.details.dialogs.DetailsCastCrewSheet
import com.free.tvtracker.ui.details.dialogs.DetailsEpisodesSheet
import com.free.tvtracker.ui.details.dialogs.DetailsFilmCollectionSheet
import com.free.tvtracker.ui.details.dialogs.DetailsMediaSheet
import com.free.tvtracker.ui.discover.DiscoverScreen
import com.free.tvtracker.ui.discover.DiscoverScreenNavActions
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.discover.RecommendedScreen
import com.free.tvtracker.ui.discover.RecommendedScreenNavActions
import com.free.tvtracker.ui.discover.dialogs.DiscoverNewReleasesSheet
import com.free.tvtracker.ui.discover.dialogs.DiscoverTrendingSheet
import com.free.tvtracker.ui.finished.FinishedScreen
import com.free.tvtracker.ui.finished.FinishedScreenNavAction
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.person.PersonScreen
import com.free.tvtracker.ui.person.PersonScreenNavAction
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.person.dialogs.PersonMoviesSheet
import com.free.tvtracker.ui.person.dialogs.PersonPhotosSheet
import com.free.tvtracker.ui.person.dialogs.PersonShowsSheet
import com.free.tvtracker.ui.search.AddTrackedScreen
import com.free.tvtracker.ui.search.AddTrackedScreenNavAction
import com.free.tvtracker.ui.search.AddTrackedScreenOriginScreen
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.login.LoginScreen
import com.free.tvtracker.ui.settings.login.LoginScreenNavAction
import com.free.tvtracker.ui.settings.SettingsScreen
import com.free.tvtracker.ui.settings.SettingsScreenNavAction
import com.free.tvtracker.ui.settings.SettingsViewModel
import com.free.tvtracker.ui.settings.signup.SignupScreen
import com.free.tvtracker.ui.settings.signup.SignupScreenAction
import com.free.tvtracker.ui.settings.login.LoginViewModel
import com.free.tvtracker.ui.settings.signup.SignupViewModel
import com.free.tvtracker.ui.splash.SplashErrorScreen
import com.free.tvtracker.ui.watching.WatchingScreen
import com.free.tvtracker.ui.watching.WatchingScreenNavAction
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlist.WatchlistScreen
import com.free.tvtracker.ui.watchlist.WatchlistScreenNavAction
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel
import com.free.tvtracker.ui.welcome.WelcomeScreen
import com.free.tvtracker.ui.welcome.WelcomeViewModel

fun SplashErrorScreenViewController() =
    ComposeUIViewController {
        SplashErrorScreen()
    }

fun WelcomeScreenViewController(navigateHome: () -> Unit, viewModel: WelcomeViewModel) =
    ComposeUIViewController {
        WelcomeScreen(navigateHome, viewModel)
    }

fun LoginScreenViewController(nav: (LoginScreenNavAction) -> Unit, viewModel: LoginViewModel) =
    ComposeUIViewController(
        configure = {
            onFocusBehavior = OnFocusBehavior.DoNothing
        }
    ) {
        LoginScreen(viewModel, nav)
    }

fun SignupScreenViewController(nav: (SignupScreenAction) -> Unit, viewModel: SignupViewModel) =
    ComposeUIViewController(
        configure = {
            onFocusBehavior = OnFocusBehavior.DoNothing
        }
    ) {
        SignupScreen(viewModel, nav)
    }

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
        DiscoverScreen(viewModel, navigate)
    }

fun RecommendedScreenViewController(navigate: (RecommendedScreenNavActions) -> Unit, viewModel: DiscoverViewModel) =
    ComposeUIViewController {
        RecommendedScreen(viewModel, navigate)
    }

fun TrendingScreenViewController(navigate: (DiscoverScreenNavActions) -> Unit, viewModel: DiscoverViewModel) =
    ComposeUIViewController {
        DiscoverTrendingSheet(viewModel, navigate)
    }

fun NewReleasesScreenViewController(navigate: (DiscoverScreenNavActions) -> Unit, viewModel: DiscoverViewModel) =
    ComposeUIViewController {
        DiscoverNewReleasesSheet(viewModel, navigate)
    }

fun SettingsScreenViewController(navigate: (SettingsScreenNavAction) -> Unit, viewModel: SettingsViewModel) =
    ComposeUIViewController(
        configure = {
            onFocusBehavior = OnFocusBehavior.DoNothing
        }
    ) {
        SettingsScreen(viewModel, navigate, PaddingValues(0.dp))
    }

fun ShowDetailsScreenViewController(
    detailsViewModel: DetailsViewModel,
    content: DetailsViewModel.LoadContent,
    navigate: (DetailsScreenNavAction) -> Unit
) = ComposeUIViewController {
    DetailsScreen(detailsViewModel, content, navigate)
}

fun DetailsEpisodesViewController(viewModel: DetailsViewModel) =
    ComposeUIViewController {
        DetailsEpisodesSheet(viewModel)
    }

fun DetailsMediaViewController(viewModel: DetailsViewModel, navigate: (DetailsScreenNavAction) -> Unit) =
    ComposeUIViewController {
        DetailsMediaSheet(viewModel, navigate)
    }

fun DetailsCastCrewViewController(viewModel: DetailsViewModel, navigate: (DetailsScreenNavAction) -> Unit) =
    ComposeUIViewController {
        DetailsCastCrewSheet(viewModel, navigate)
    }

fun PersonDetailsViewController(viewModel: PersonViewModel, personId: Int, navigate: (PersonScreenNavAction) -> Unit) =
    ComposeUIViewController {
        PersonScreen(viewModel, personId, navigate)
    }

fun PersonShowsViewController(viewModel: PersonViewModel, navigate: (PersonScreenNavAction) -> Unit) =
    ComposeUIViewController {
        PersonShowsSheet(viewModel, navigate)
    }

fun PersonMoviesViewController(viewModel: PersonViewModel, navigate: (PersonScreenNavAction) -> Unit) =
    ComposeUIViewController {
        PersonMoviesSheet(viewModel, navigate)
    }

fun PersonPhotosViewController(viewModel: PersonViewModel) =
    ComposeUIViewController {
        PersonPhotosSheet(viewModel)
    }

fun DetailsFilmCollectionViewController(viewModel: DetailsViewModel, navigate: (DetailsScreenNavAction) -> Unit) =
    ComposeUIViewController {
        DetailsFilmCollectionSheet(viewModel, navigate)
    }

fun AddTrackedScreenViewController(
    addTrackedViewModel: AddTrackedViewModel,
    navigate: (AddTrackedScreenNavAction) -> Unit,
    originScreen: AddTrackedScreenOriginScreen
) = ComposeUIViewController(
    configure = {
        onFocusBehavior = OnFocusBehavior.DoNothing
    }
) {
    AddTrackedScreen(addTrackedViewModel, navigate, originScreen)
}
