package com.free.tvtracker.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.screens.watching.WatchingItemUiModel
import com.free.tvtracker.screens.watching.WatchingOk
import com.free.tvtracker.screens.watching.WatchingUiState
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.screens.watching.WatchingEmpty


@Preview
@Composable
fun WatchingEmptyPreview() {
    TvTrackerTheme {
        WatchingEmpty(navigate = {})
    }
}

@Preview
@Composable
fun WatchingOkNoWaitingPreview() {
    TvTrackerTheme {
        WatchingOk(
            { },
            { a, b -> },
            WatchingUiState.Ok(
                watching = listOf(
                    WatchingItemUiModel(
                        trackedShowId = 1,
                        tmdbId = 1,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = "b",
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                    ),
                    WatchingItemUiModel(
                        trackedShowId = 1,
                        tmdbId = 1,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = "a",
                            body = "Watch next",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        )
                    )
                ),
                waitingNextEpisode = emptyList()
            ),
        )
    }
}

@Preview
@Composable
fun WatchingOkWithWaitingPreview() {
    TvTrackerTheme {
        WatchingOk(
            { },
            { a, b -> },
            WatchingUiState.Ok(
                watching = listOf(
                    WatchingItemUiModel(
                        trackedShowId = 1,
                        tmdbId = 1,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = "b",
                            body = "Watch next",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                    ),
                    WatchingItemUiModel(
                        trackedShowId = 1,
                        tmdbId = 1,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = "a",
                            body = "Watch next",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        )
                    )
                ),
                waitingNextEpisode = listOf(
                    WatchingItemUiModel(
                        trackedShowId = 1,
                        tmdbId = 1,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = "b",
                            body = "Watch next",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                    )
                )
            ),
        )
    }
}


@Preview
@Composable
fun WatchingOkNoWatchablePreview() {
    TvTrackerTheme {
        WatchingOk(
            { },
            { a, b -> },
            WatchingUiState.Ok(
                watching = emptyList(),
                waitingNextEpisode = listOf(
                    WatchingItemUiModel(
                        trackedShowId = 1,
                        tmdbId = 1,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = "b",
                            body = "Watch next",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                    )
                )
            ),
        )
    }
}

