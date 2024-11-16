package com.free.tvtracker.activities.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.free.tvtracker.activities.person.PersonDetailsActivity
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.search.AddTrackedScreen
import com.free.tvtracker.ui.search.AddTrackedScreenNavAction
import com.free.tvtracker.ui.search.AddTrackedScreenOriginScreen
import com.free.tvtracker.ui.search.AddTrackedViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
class AddTrackedActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context, origin: AddTrackedScreenOriginScreen) =
            Intent(context, AddTrackedActivity::class.java).apply {
                putExtra(
                    EXTRA_ORIGIN_SCREEN, origin.ordinal
                )
            }

        const val EXTRA_ORIGIN_SCREEN = "EXTRA_ORIGIN_SCREEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AddTrackedViewModel = koinViewModel()
            val origin = AddTrackedScreenOriginScreen.entries[intent.getIntExtra(
                EXTRA_ORIGIN_SCREEN,
                AddTrackedScreenOriginScreen.Watching.ordinal
            )]
            val context = LocalContext.current as AddTrackedActivity
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            val navActions = { action: AddTrackedScreenNavAction ->
                when (action) {
                    is AddTrackedScreenNavAction.GoContentDetails -> {
                        context.startActivity(
                            ShowDetailsActivity.create(context, action.showTmdbId, action.isTvShow)
                        )
                    }

                    is AddTrackedScreenNavAction.GoPersonDetails -> {
                        context.startActivity(
                            Intent(
                                context,
                                PersonDetailsActivity::class.java
                            ).putExtra(PersonDetailsActivity.EXTRA_PERSON_ID, action.personTmdbId)
                        )
                    }

                }
            }
            val title = when (origin) {
                AddTrackedScreenOriginScreen.Watching -> "Add to currently watching"
                AddTrackedScreenOriginScreen.Finished -> "Add to finished watching"
                AddTrackedScreenOriginScreen.Watchlist -> "Add to watchlist"
                AddTrackedScreenOriginScreen.Discover -> "Search"
            }
            TvTrackerTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = title) },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.topAppBarColors(),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            },
                        )
                    }
                ) { padding ->
                    AddTrackedScreen(
                        viewModel = viewModel,
                        navActions = navActions,
                        originScreen = origin,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
