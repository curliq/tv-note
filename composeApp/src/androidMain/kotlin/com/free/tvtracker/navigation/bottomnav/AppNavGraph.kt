package com.free.tvtracker.navigation.bottomnav

import android.app.Activity
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.free.tvtracker.activities.AddShowActivity
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity.Extras.EXTRA_SHOW_ID
import com.free.tvtracker.navigation.AppNavController
import com.free.tvtracker.screens.discover.DiscoverScreen
import com.free.tvtracker.screens.finished.FinishedScreen
import com.free.tvtracker.screens.finished.FinishedScreenNavAction
import com.free.tvtracker.screens.settings.SettingsScreen
import com.free.tvtracker.screens.watching.WatchingScreenNavAction
import com.free.tvtracker.screens.watching.WatchingScreen
import com.free.tvtracker.screens.watchlist.WatchlistScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.mainNavGraph(navController: AppNavController, context: Activity) {
    composable(AppNavDestinations.WATCHING.id) {
        WatchingScreen(
            navigate = { action ->
                when (action) {
                    WatchingScreenNavAction.GoAddShow -> context.startActivity(
                        Intent(
                            context,
                            AddShowActivity::class.java
                        )
                    )

                    is WatchingScreenNavAction.GoShowDetails -> context.startActivity(
                        Intent(
                            context,
                            ShowDetailsActivity::class.java
                        ).putExtra(EXTRA_SHOW_ID, action.tmdbShowId)
                    )
                }
            },
            viewModel = koinViewModel()
        )
    }
    composable(AppNavDestinations.FINISHED.id) {
        FinishedScreen(
            navigate = { action ->
                when (action) {
                    FinishedScreenNavAction.GoAddShow -> TODO()
                    is FinishedScreenNavAction.GoShowDetails -> {
                        context.startActivity(
                            Intent(
                                context,
                                ShowDetailsActivity::class.java
                            ).putExtra(EXTRA_SHOW_ID, action.tmdbShowId)
                        )
                    }
                }
            },
            viewModel = koinViewModel()
        )
    }
    composable(AppNavDestinations.WATCHLIST.id) {
        WatchlistScreen(viewModel = koinViewModel())
    }
    composable(AppNavDestinations.DISCOVER.id) {
        DiscoverScreen()
    }
    composable(AppNavDestinations.SETTINGS.id) {
        SettingsScreen()
    }
}
