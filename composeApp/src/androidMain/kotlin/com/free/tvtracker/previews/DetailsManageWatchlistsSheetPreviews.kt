package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.previews.DetailsScreenPreviews.showDetailsUiModel
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.details.dialogs.DetailsManageWatchlistsContent
import com.free.tvtracker.ui.watchlists.list.WatchlistsUiModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview()
@Composable
fun DetailsManageWatchlistsSheetPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsManageWatchlistsContent(
                showDetailsUiModel,
                DetailsViewModel.DetailsWatchlists(
                    listOf(
                        DetailsViewModel.DetailsWatchlists.Item(
                            WatchlistsUiModel(1, "Watchlist", emptyList(), 1, 1), true
                        ),
                        DetailsViewModel.DetailsWatchlists.Item(
                            WatchlistsUiModel(1, "Finished", emptyList(), 1, 1), true
                        ),
                        DetailsViewModel.DetailsWatchlists.Item(
                            WatchlistsUiModel(1, "list", emptyList(), 1, 1), true
                        ),
                    )
                ), { }, { }
            )
        }
    }
}
