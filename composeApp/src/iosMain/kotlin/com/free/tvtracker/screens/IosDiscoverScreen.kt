package com.free.tvtracker.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.screens.discover.DiscoverScreen
import com.free.tvtracker.screens.discover.DiscoverScreenNavActions
import com.free.tvtracker.screens.discover.DiscoverViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IosDiscoverScreen(viewModel: DiscoverViewModel, navigate: (DiscoverScreenNavActions) -> Unit) {
    val scroll = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scroll.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = "Discover",
                        style = TvTrackerTheme.Typography.headlineMedium
                    )
                },
                scrollBehavior = scroll,
            )
        }
    ) { padding ->
        DiscoverScreen(viewModel, navigate, padding)
    }
}
