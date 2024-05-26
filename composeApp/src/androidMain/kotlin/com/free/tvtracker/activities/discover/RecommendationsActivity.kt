package com.free.tvtracker.activities.discover

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.screens.discover.DiscoverViewModel
import com.free.tvtracker.screens.discover.RecommendedScreen
import com.free.tvtracker.screens.discover.RecommendedScreenNavActions
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
class RecommendationsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: DiscoverViewModel = get()
            val context = LocalContext.current as RecommendationsActivity
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            val navActions = { action: RecommendedScreenNavActions ->
                when (action) {
                    is RecommendedScreenNavActions.GoShowDetails -> {
                        context.startActivity(
                            Intent(
                                context,
                                ShowDetailsActivity::class.java
                            ).putExtra(ShowDetailsActivity.EXTRA_SHOW_ID, action.tmdbShowId)
                        )
                    }
                }
            }
            TvTrackerTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        MediumTopAppBar(
                            title = { Text(text = "Recommended") },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.mediumTopAppBarColors(),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            }
                        )
                    }
                ) { padding ->
                    RecommendedScreen(
                        viewModel = viewModel,
                        navigate = navActions,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
