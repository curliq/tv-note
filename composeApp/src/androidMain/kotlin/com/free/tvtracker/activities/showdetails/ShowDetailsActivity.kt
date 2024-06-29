package com.free.tvtracker.activities.showdetails

import android.content.Context
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
import com.free.tvtracker.activities.person.PersonDetailsActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.details.DetailsScreen
import com.free.tvtracker.ui.details.DetailsScreenNavAction
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.details.dialogs.DetailsCastCrewSheet
import com.free.tvtracker.ui.details.dialogs.DetailsEpisodesSheet
import com.free.tvtracker.ui.details.dialogs.DetailsMediaSheet
import org.koin.androidx.compose.koinViewModel


class ShowDetailsActivity : BaseActivity() {

    companion object {
        const val EXTRA_SHOW_ID = "EXTRA_SHOW_ID"
        const val EXTRA_IS_SHOW = "EXTRA_IS_SHOW"
        fun create(context:Context, tmdbId:Int, isShow:Boolean): Intent {
            return Intent(context, ShowDetailsActivity::class.java).apply {
                putExtra(EXTRA_SHOW_ID, tmdbId)
                putExtra(EXTRA_IS_SHOW, isShow)
            }
        }
    }

    enum class ShowDetailsNavDestinations {
        EPISODES,
        MEDIA,
        CASTCREW,
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val showId = intent.getIntExtra(EXTRA_SHOW_ID, -1)
        val isContentTvShow = intent.getBooleanExtra(EXTRA_IS_SHOW, true)
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

                    is DetailsScreenNavAction.GoCastAndCrewDetails -> {
                        startActivity(
                            Intent(
                                context,
                                PersonDetailsActivity::class.java
                            ).putExtra(PersonDetailsActivity.EXTRA_PERSON_ID, action.personTmdbId)
                        )
                    }
                }
            }
            TvTrackerTheme {
                val viewModel: DetailsViewModel = koinViewModel(owner = context)
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                        content = DetailsViewModel.LoadContent(showId, isContentTvShow),
                        navAction = navActions,
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
