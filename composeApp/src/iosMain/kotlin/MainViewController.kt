import androidx.compose.ui.window.ComposeUIViewController
import besttvtracker.DiscoverScreen
import besttvtracker.FinishedScreen
import besttvtracker.SettingsScreen
import besttvtracker.ShowDetailsScreen
import besttvtracker.WatchingScreen
import besttvtracker.WatchlistScreen

fun WatchingScreenViewController(onNav: () -> Unit) = ComposeUIViewController {
    WatchingScreen(onNav)
}

fun FinishedScreenViewController() = ComposeUIViewController {
    FinishedScreen({})
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
    ShowDetailsScreen()
}
