package com.free.tvtracker.activities.main.bottomnav

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.free.tvtracker.activities.add.AddTrackedActivity
import com.free.tvtracker.activities.discover.RecommendationsActivity
import com.free.tvtracker.activities.main.AppNavController
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity
import com.free.tvtracker.activities.watchlists.WatchlistDetailsActivity
import com.free.tvtracker.ui.discover.DiscoverScreen
import com.free.tvtracker.ui.discover.DiscoverScreenNavActions
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.discover.dialogs.DiscoverNewReleasesSheet
import com.free.tvtracker.ui.discover.dialogs.DiscoverTrendingSheet
import com.free.tvtracker.ui.search.AddTrackedScreenOriginScreen
import com.free.tvtracker.ui.watching.WatchingScreen
import com.free.tvtracker.ui.watching.WatchingScreenNavAction
import com.free.tvtracker.ui.watchlists.list.WatchlistsScreen
import com.free.tvtracker.ui.watchlists.list.WatchlistsScreenNavAction
import com.free.tvtracker.ui.watchlists.list.WatchlistsViewModel
import com.free.tvtracker.ui.watchlists.list.dialogs.WatchlistAddSheet
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
fun NavGraphBuilder.mainNavGraph(
    navController: AppNavController,
    showBottomSheet: MutableState<WatchlistNavDestinations?>,
    context: Activity
) {
    composable(AppNavDestinations.WATCHING.id) {
        WatchingScreen(
            navigate = { action ->
                when (action) {
                    WatchingScreenNavAction.GoAddShow -> context.startActivity(
                        AddTrackedActivity.createIntent(
                            context,
                            AddTrackedScreenOriginScreen.Watching
                        )
                    )

                    is WatchingScreenNavAction.GoShowDetails -> {
                        context.startActivity(ShowDetailsActivity.create(context, action.tmdbShowId, action.isTvShow))
                    }
                }
            },
            viewModel = koinViewModel()
        )
    }
    composable(AppNavDestinations.WATCHLISTS.id) {
        val discoverViewModel: WatchlistsViewModel = koinViewModel()
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        val navActions: (WatchlistsScreenNavAction) -> Unit = { action ->
            when (action) {
                WatchlistsScreenNavAction.GoAddShow -> context.startActivity(
                    AddTrackedActivity.createIntent(
                        context,
                        AddTrackedScreenOriginScreen.Watchlist
                    )
                )

                is WatchlistsScreenNavAction.GoWatchlistDetails -> context.startActivity(
                    WatchlistDetailsActivity.create(
                        context,
                        action.watchlistId,
                        action.watchlistName
                    )
                )

                WatchlistsScreenNavAction.HideBottomSheet -> {
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet.value = null
                    }
                }
            }
        }
        WatchlistsScreen(
            viewModel = discoverViewModel,
            navigate = navActions,
        )
        val modalMaxHeight = LocalWindowInfo.current.containerSize.height.dp.times(0.7f)
        if (showBottomSheet.value != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = null
                },
                sheetState = sheetState,
                contentWindowInsets = { WindowInsets(0, 0, 0, 0) } // draw behind navbar
            ) {
                Box(Modifier.heightIn(0.dp, modalMaxHeight)) {
                    when (showBottomSheet.value) {
                        WatchlistNavDestinations.ADD_WATCHLIST -> {
                            WatchlistAddSheet(
                                viewModel = discoverViewModel,
                                navAction = navActions,
                                bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding().value
                            )
                        }

                        null -> {}
                    }
                }
            }
        }
    }
    composable(AppNavDestinations.DISCOVER.id) {
        var showBottomSheet: DiscoverNavDestinations? by remember { mutableStateOf(null) }
        val action = { action: DiscoverScreenNavActions ->
            when (action) {
                DiscoverScreenNavActions.GoAddShow -> {
                    context.startActivity(
                        AddTrackedActivity.createIntent(
                            context = context,
                            origin = AddTrackedScreenOriginScreen.Discover
                        )
                    )
                }

                is DiscoverScreenNavActions.GoShowDetails -> {
                    context.startActivity(
                        ShowDetailsActivity.create(context, action.tmdbShowId, action.isTvShow)
                    )
                }

                DiscoverScreenNavActions.GoRecommendations -> {
                    context.startActivity(
                        Intent(
                            context,
                            RecommendationsActivity::class.java
                        )
                    )
                }

                DiscoverScreenNavActions.GoTrending -> {
                    showBottomSheet = DiscoverNavDestinations.TRENDING
                }

                DiscoverScreenNavActions.GoNewRelease -> {
                    showBottomSheet = DiscoverNavDestinations.RELEASES_SOON
                }
            }
        }
        val discoverViewModel: DiscoverViewModel = get() // get() instead of viewmodel() to share between activities
        DiscoverScreen(viewModel = discoverViewModel, action)
        val sheetState = rememberModalBottomSheetState()
        val modalMaxHeight = LocalWindowInfo.current.containerSize.height.dp.times(0.7f)
        if (showBottomSheet != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = null
                },
                sheetState = sheetState,
                contentWindowInsets = { WindowInsets(0, 0, 0, 0) } // draw behind navbar
            ) {
                Box(Modifier.heightIn(0.dp, modalMaxHeight)) {
                    when (showBottomSheet) {
                        DiscoverNavDestinations.TRENDING -> {
                            DiscoverTrendingSheet(
                                viewModel = discoverViewModel,
                                navActions = action,
                                bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding().value
                            )
                        }

                        DiscoverNavDestinations.RELEASES_SOON -> {
                            DiscoverNewReleasesSheet(
                                viewModel = discoverViewModel,
                                navActions = action,
                                bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding().value
                            )
                        }

                        null -> {}
                    }
                }
            }
        }
    }
}

enum class DiscoverNavDestinations {
    TRENDING,
    RELEASES_SOON
}

enum class WatchlistNavDestinations {
    ADD_WATCHLIST
}
