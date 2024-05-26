package com.free.tvtracker.screens.discover.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.screens.discover.DiscoverScreenNavActions
import com.free.tvtracker.screens.discover.DiscoverUiState
import com.free.tvtracker.screens.discover.DiscoverViewModel

@Composable
fun DiscoverNewReleasesSheet(
    viewModel: DiscoverViewModel,
    navActions: (DiscoverScreenNavActions) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.uiModel.collectAsState().value as DiscoverUiState.Ok
    TvTrackerTheme {
        DiscoverTrendingSheetContent(show.uiModel.showsReleasedSoon, navActions, bottomPadding)
    }
}
