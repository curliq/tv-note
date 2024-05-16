package com.free.tvtracker.activities.showdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.screens.details.DetailsScreen
import com.free.tvtracker.screens.details.DetailsScreenNavAction
import com.free.tvtracker.screens.details.DetailsViewModel
import com.free.tvtracker.screens.details.dialogs.DetailsCastCrewSheet
import com.free.tvtracker.screens.details.dialogs.DetailsEpisodesSheet
import com.free.tvtracker.screens.details.dialogs.DetailsMediaSheet
import org.koin.androidx.compose.koinViewModel


class ShowDetailsActivity : BaseActivity() {
    companion object Extras {
        const val EXTRA_SHOW_ID = "EXTRA_SHOW_ID"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val showId = intent.getIntExtra(EXTRA_SHOW_ID, -1)
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val sheetState = rememberModalBottomSheetState()
            var showBottomSheet: ShowDetailsNavDestinations? by remember { mutableStateOf(null) }
            val context = LocalContext.current as ShowDetailsActivity
            val modalMaxHeight = LocalConfiguration.current.screenHeightDp.dp.times(0.7f)
            val navActions: (DetailsScreenNavAction) -> Unit = { action ->
                when (action) {
                    DetailsScreenNavAction.GoAllEpisodes -> {
                        showBottomSheet = ShowDetailsNavDestinations.EPISODES
                    }

                    is DetailsScreenNavAction.GoYoutube -> {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(action.webUrl)
                            )
                        )
                    }

                    DetailsScreenNavAction.GoMedia -> {
                        showBottomSheet = ShowDetailsNavDestinations.MEDIA
                    }
                    DetailsScreenNavAction.GoCastAndCrew -> {
                        showBottomSheet = ShowDetailsNavDestinations.CASTCREW
                    }
                }
            }
            TvTrackerTheme {
                val viewModel: DetailsViewModel = koinViewModel(owner = context)
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.mediumTopAppBarColors(),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, viewModel.getShareLink())
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    startActivity(shareIntent)
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Share,
                                        contentDescription = "Share"
                                    )
                                }
                            },
                        )
                    },
                ) { padding ->
                    DetailsScreen(
                        viewModel = viewModel,
                        showId = showId,
                        navActions,
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
                            windowInsets = WindowInsets(0, 0, 0, 0) // draw behind navbar
                        ) {
                            Box(Modifier.heightIn(0.dp, modalMaxHeight)) {
                                when (showBottomSheet) {
                                    ShowDetailsNavDestinations.EPISODES -> {
                                        DetailsEpisodesSheet(
                                            viewModel = koinViewModel(owner = context),
                                            padding.calculateBottomPadding().value
                                        )
                                    }
                                    ShowDetailsNavDestinations.MEDIA -> {
                                        DetailsMediaSheet(
                                            viewModel = koinViewModel(owner = context),
                                            navActions,
                                            padding.calculateBottomPadding().value
                                        )
                                    }
                                    ShowDetailsNavDestinations.CASTCREW -> {
                                        DetailsCastCrewSheet(
                                            viewModel = koinViewModel(owner = context),

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
