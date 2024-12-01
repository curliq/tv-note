package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.finished.FinishedEmpty
import com.free.tvtracker.ui.finished.FinishedOk
import com.free.tvtracker.ui.finished.FinishedShowUiModel
import com.free.tvtracker.ui.finished.FinishedUiState

@Preview
@Composable
fun FinishedPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            FinishedOk(
                FinishedUiState.Ok(
                    listOf(
                        FinishedShowUiModel(
                            tmdbId = 1,
                            title = "Game of thrones",
                            image = "",
                            status = "ended",
                            true
                        ),
                    ),
                    filterTvShows = true,
                ),
                PurchaseStatus(PurchaseStatus.Status.Purchased, "123"),
                {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun FinishedEmptyPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            FinishedEmpty(
                PurchaseStatus(PurchaseStatus.Status.Purchased, "123"),
                {}
            )
        }
    }
}

@Preview
@Composable
fun FinishedEmptyTrialPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            FinishedEmpty(
                PurchaseStatus(PurchaseStatus.Status.TrialOn, "123"),
                {}
            )
        }
    }
}

@Preview
@Composable
fun FinishedOkTrialPreview() {
    TvTrackerTheme {
        Scaffold { p ->
            FinishedOk(
                FinishedUiState.Ok(
                    listOf(
                        FinishedShowUiModel(
                            tmdbId = 1,
                            title = "Game of thrones",
                            image = "",
                            status = "ended",
                            true
                        ),
                    ),
                    filterTvShows = true,
                ),
                PurchaseStatus(PurchaseStatus.Status.TrialFinished, "123"),
                {},
                {}
            )
        }
    }
}
