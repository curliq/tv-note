package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.watchlist.WatchlistEmpty
import com.free.tvtracker.ui.watchlist.WatchlistOk
import com.free.tvtracker.ui.watchlist.WatchlistShowUiModel
import com.free.tvtracker.ui.watchlist.WatchlistUiState

@Preview
@Composable
fun WatchlistPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            WatchlistOk(
                WatchlistUiState.Ok(
                    listOf(
                        WatchlistShowUiModel(
                            tmdbId = 1,
                            title = "Game of thrones",
                            image = "",
                            status = "ended",
                            true
                        ),
                    ),
                    filterTvShows = true,
                ),
                PurchaseStatus(PurchaseStatus.Status.Purchased, "123", ""),
                {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchlistEmptyPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            WatchlistEmpty(
                PurchaseStatus(PurchaseStatus.Status.Purchased, "123", ""),
                {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchlistEmptyTrialPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            WatchlistEmpty(
                PurchaseStatus(PurchaseStatus.Status.TrialOn, "123", ""),
                {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchlistOkTrialPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            WatchlistOk(
                WatchlistUiState.Ok(
                    listOf(
                        WatchlistShowUiModel(
                            tmdbId = 1,
                            title = "Game of thrones",
                            image = "",
                            status = "ended",
                            true
                        ),
                    ),
                    filterTvShows = true,
                ),
                PurchaseStatus(PurchaseStatus.Status.TrialFinished, "123", ""),
                {},
                {}
            )
        }
    }
}
