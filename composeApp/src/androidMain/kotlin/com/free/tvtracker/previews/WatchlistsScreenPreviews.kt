package com.free.tvtracker.previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.watchlists.list.WatchlistEmpty
import com.free.tvtracker.ui.watchlists.list.WatchlistsOk
import com.free.tvtracker.ui.watchlists.list.WatchlistsUiModel
import com.free.tvtracker.ui.watchlists.list.WatchlistsUiState

@Preview
@Composable
fun WatchlistsEmptyPreview() {
    TvTrackerTheme {
        Surface {
            WatchlistEmpty(PurchaseStatus(PurchaseStatus.Status.Purchased, "£2.99", "£0.99"), {}, {})
        }
    }
}

@Preview
@Composable
fun WatchlistsOkPreview() {
    TvTrackerTheme {
        Surface {
            WatchlistsOk(
                WatchlistsUiState.Ok(
                    watchlists = listOf(
                        WatchlistsUiModel(
                            1, "finished",
                            listOf("", ""),
                            3, 3
                        ),
                        WatchlistsUiModel(
                            2, "wont watch",
                            listOf("", ""),
                            3, 3
                        )
                    )
                ),
                PurchaseStatus(PurchaseStatus.Status.Purchased, "£2.99", "£0.99"), {}, {}
            )
        }
    }
}


@Preview
@Composable
fun WatchlistsOkNoPaidPreview() {
    TvTrackerTheme {
        Surface {
            WatchlistsOk(
                WatchlistsUiState.Ok(
                    watchlists = listOf(
                        WatchlistsUiModel(
                            1, "finished",
                            listOf("", ""),
                            3, 3
                        ),
                        WatchlistsUiModel(
                            2, "wont watch",
                            listOf("", ""),
                            3, 3
                        )
                    )
                ),
                PurchaseStatus(PurchaseStatus.Status.TrialFinished, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}
