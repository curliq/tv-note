package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
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
                    filterMovies = false
                ),
                {},
                {}
            )
        }
    }
}
