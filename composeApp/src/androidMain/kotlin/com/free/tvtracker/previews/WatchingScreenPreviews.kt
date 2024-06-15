package com.free.tvtracker.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.watching.WatchingItemUiModel
import com.free.tvtracker.ui.watching.WatchingOk
import com.free.tvtracker.ui.watching.WatchingUiState
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.watching.WatchingEmpty


@Preview
@Composable
fun WatchingEmptyPreview() {
    TvTrackerTheme {
        WatchingEmpty()
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
                            id = 2,
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                        nextEpisodeCountdown = "null"
                    ),
                    WatchingItemUiModel(
                        trackedShowId = 2,
                        tmdbId = 2,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = 1,
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                        nextEpisodeCountdown = "null"
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
                            id = 2,
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                        nextEpisodeCountdown = "null"
                    ),
                    WatchingItemUiModel(
                        trackedShowId = 2,
                        tmdbId = 2,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = 1,
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                        nextEpisodeCountdown = "null"
                    )
                ),
                waitingNextEpisode = listOf(
                    WatchingItemUiModel(
                        trackedShowId = 3,
                        tmdbId = 3,
                        title = "show.storedShow.title",
                        image = "",
                        nextEpisode = WatchingItemUiModel.NextEpisode(
                            id = 2,
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                        nextEpisodeCountdown = "null"
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
                            id = 2,
                            body = "Watch next ",
                            episode = "E1",
                            episodeNumber = 1,
                            season = "S1",
                            seasonNumber = 1,
                        ),
                        nextEpisodeCountdown = "null"
                    )
                )
            ),
        )
    }
}

