import androidx.compose.ui.window.ComposeUIViewController
import besttvtracker.screens.details.DetailsScreen
import besttvtracker.screens.discover.DiscoverScreen
import besttvtracker.screens.finished.FinishedScreen
import besttvtracker.screens.settings.SettingsScreen
import besttvtracker.screens.watching.WatchingScreen
import besttvtracker.screens.watchlist.WatchlistScreen

fun WatchingScreenViewController(onNav: () -> Unit) = ComposeUIViewController {
    WatchingScreen(onNav)
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

fun ShowDetailsScreenViewController() = ComposeUIViewController {
    DetailsScreen()
}
