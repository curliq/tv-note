package com.free.tvtracker.ui.discover.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.discover.DiscoverScreenNavActions
import com.free.tvtracker.ui.discover.DiscoverUiState
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel.DiscoverViewModelAction

@Composable
fun DiscoverNewReleasesSheet(
    viewModel: DiscoverViewModel,
    navActions: (DiscoverScreenNavActions) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.data.collectAsState().value as DiscoverUiState.Ok
    TvTrackerTheme {
        DiscoverTrendingSheetContent(show.uiModel.contentReleasedSoon.data, navActions, {
            viewModel.action(
                DiscoverViewModelAction.LoadPageNewReleases
            )
        }, bottomPadding)
    }
}
