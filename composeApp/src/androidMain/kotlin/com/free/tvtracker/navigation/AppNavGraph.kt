package com.free.tvtracker.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import besttvtracker.screens.discover.DiscoverScreen
import besttvtracker.screens.finished.FinishedScreen
import besttvtracker.screens.settings.SettingsScreen
import besttvtracker.screens.watching.WatchingScreen
import besttvtracker.screens.watchlist.WatchlistScreen
import com.free.tvtracker.ShowDetailsActivity
import com.free.tvtracker.navigation.AppNavController.NavDestinations
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.mainNavGraph(navController: AppNavController, context: Activity) {
    composable(NavDestinations.WATCHING.id) {
        WatchingScreen({
            context.startActivity(Intent(context, ShowDetailsActivity::class.java))
        }, viewModel = viewModel())
    }
    composable(NavDestinations.FINISHED.id) {
        FinishedScreen()
    }
    composable(NavDestinations.WATCHLIST.id) {
        WatchlistScreen()
    }
    composable(NavDestinations.DISCOVER.id) {
        DiscoverScreen()
    }
    composable(NavDestinations.SETTINGS.id) {
        SettingsScreen()
    }
}
