package com.free.tvtracker.activities.watchlists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.free.tvtracker.activities.add.AddTrackedActivity
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.search.AddTrackedScreenOriginScreen
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsScreen
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsScreenNavAction
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsViewModel
import com.free.tvtracker.ui.watchlists.details.dialogs.WatchlistDetailsMenuSheet
import com.free.tvtracker.ui.watchlists.details.dialogs.WatchlistDetailsRenameSheet
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class WatchlistDetailsActivity : BaseActivity() {

    companion object {
        const val EXTRA_WATCHLIST_ID = "EXTRA_WATCHLIST_ID"
        const val EXTRA_WATCHLIST_NAME = "EXTRA_WATCHLIST_NAME"
        fun create(context: Context, watchlistId: Int, name: String): Intent {
            return Intent(context, WatchlistDetailsActivity::class.java).apply {
                putExtra(EXTRA_WATCHLIST_ID, watchlistId)
                putExtra(EXTRA_WATCHLIST_NAME, name)
            }
        }
    }

    enum class ShowDetailsNavDestinations {
        EDIT_MENU,
        RENAME
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val watchlistId = intent.getIntExtra(EXTRA_WATCHLIST_ID, -1)
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val sheetState = rememberModalBottomSheetState()
            var showBottomSheet: ShowDetailsNavDestinations? by remember { mutableStateOf(null) }
            val context = LocalActivity.current as WatchlistDetailsActivity
            val scope = rememberCoroutineScope()
            val modalMaxHeight = LocalWindowInfo.current.containerSize.height.dp.times(0.7f)
            val navActions: (WatchlistDetailsScreenNavAction) -> Unit = { action ->
                when (action) {
                    is WatchlistDetailsScreenNavAction.GoShowDetails -> {
                        startActivity(ShowDetailsActivity.create(context, action.tmdbShowId, action.isTvShow))
                    }

                    WatchlistDetailsScreenNavAction.GoAddShow -> context.startActivity(
                        AddTrackedActivity.createIntent(
                            context,
                            AddTrackedScreenOriginScreen.Watchlist
                        )
                    )

                    is WatchlistDetailsScreenNavAction.ShowRenameDialog -> {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = null
                            showBottomSheet = ShowDetailsNavDestinations.RENAME
                            sheetState.show()
                        }
                    }

                    WatchlistDetailsScreenNavAction.HideBottomSheet -> {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = null
                        }
                    }

                    WatchlistDetailsScreenNavAction.OnDelete -> {
                        this.finish()
                    }
                }
            }
            TvTrackerTheme {
                val viewModel: WatchlistDetailsViewModel = koinViewModel(viewModelStoreOwner = context)
                val title = viewModel.loadContent.map { it.watchlistName }
                    .collectAsState(intent.getStringExtra(EXTRA_WATCHLIST_NAME) ?: "").value
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.mediumTopAppBarColors(),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.ArrowBack,
                                        "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    scope.launch {
                                        showBottomSheet = ShowDetailsNavDestinations.EDIT_MENU
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                        )
                    },
                ) { padding ->
                    WatchlistDetailsScreen(
                        viewModel = viewModel,
                        content = WatchlistDetailsViewModel.LoadContent(watchlistId, title),
                        navigate = navActions,
                        modifier = Modifier
                            .padding(padding)
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    )
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
                                    ShowDetailsNavDestinations.EDIT_MENU -> {
                                        WatchlistDetailsMenuSheet(
                                            viewModel = viewModel,
                                            navActions,
                                            padding.calculateBottomPadding().value
                                        )
                                    }

                                    ShowDetailsNavDestinations.RENAME -> {
                                        WatchlistDetailsRenameSheet(
                                            viewModel = viewModel,
                                            navActions,
                                            padding.calculateBottomPadding().value
                                        )
                                    }

                                    null -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
