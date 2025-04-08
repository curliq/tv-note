package com.free.tvtracker.previews

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.ui.watching.WatchingItemUiModel
import com.free.tvtracker.ui.watching.WatchingOk
import com.free.tvtracker.ui.watching.WatchingUiState
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.watching.WatchingEmpty

@Preview
@Composable
fun WatchingEmptyPreview() {
    TvTrackerTheme {
        Surface {
            WatchingEmpty(PurchaseStatus(PurchaseStatus.Status.Purchased, "£2.99", "£0.99"), {}, {})
        }
    }
}

@Preview
@Composable
fun WatchingOkNoWaitingPreview() {
    TvTrackerTheme {
        Surface {
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
                                watchNext = "Watch next ",
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
                                watchNext = "Watch next ",
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
                PurchaseStatus(PurchaseStatus.Status.Purchased, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchingOkWithWaitingPreview() {
    TvTrackerTheme {
        Surface {
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
                                watchNext = "Watch next ",
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
                                watchNext = "Watch next ",
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
                                watchNext = "Watch next ",
                                episode = "E1",
                                episodeNumber = 1,
                                season = "S1",
                                seasonNumber = 1,
                            ),
                            nextEpisodeCountdown = "null"
                        )
                    )
                ),
                PurchaseStatus(PurchaseStatus.Status.Purchased, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchingOkNoWatchablePreview() {
    TvTrackerTheme {
        Surface {
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
                                watchNext = "Watch next ",
                                episode = "E1",
                                episodeNumber = 1,
                                season = "S1",
                                seasonNumber = 1,
                            ),
                            nextEpisodeCountdown = "null"
                        )
                    )
                ),
                PurchaseStatus(PurchaseStatus.Status.Purchased, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchingEmptyTrialOnPreview() {
    TvTrackerTheme {
        Surface {
            WatchingEmpty(PurchaseStatus(PurchaseStatus.Status.TrialOn, "£2.99", "£0.99"), {}, {})
        }
    }
}

@Preview
@Composable
fun WatchingEmptyTrialFinishedPreview() {
    TvTrackerTheme {
        Surface {
            WatchingEmpty(PurchaseStatus(PurchaseStatus.Status.TrialFinished, "£2.99", "£0.99"), {}, {})
        }
    }
}

@Preview
@Composable
fun WatchingOkNoWatchableTrialOnPreview() {
    TvTrackerTheme {
        Surface {
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
                                watchNext = "Watch next ",
                                episode = "E1",
                                episodeNumber = 1,
                                season = "S1",
                                seasonNumber = 1,
                            ),
                            nextEpisodeCountdown = "null"
                        )
                    )
                ),
                PurchaseStatus(PurchaseStatus.Status.TrialFinished, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}

@Preview
@Composable
fun WatchingOkNoWaitingTrialOnPreview() {
    TvTrackerTheme {
        Surface {
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
                                watchNext = "Watch next ",
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
                PurchaseStatus(PurchaseStatus.Status.TrialFinished, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}

// happens if theres a wishlisted item
@Preview
@Composable
fun WatchingOkEmptyTrialOverPreview() {
    TvTrackerTheme {
        Surface {
            WatchingOk(
                { },
                { a, b -> },
                WatchingUiState.Ok(
                    watching = emptyList(),
                    waitingNextEpisode = emptyList()
                ),
                PurchaseStatus(PurchaseStatus.Status.TrialFinished, "£2.99", "£0.99"), {},
                {}
            )
        }
    }
}
