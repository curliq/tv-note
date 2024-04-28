package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.screens.details.dialogs.DetailsEpisodesContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1200)
@Composable
fun DetailsEpisodesSheetPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsEpisodesContent(
                DetailsUiModel(
                    tmdbId = 1,
                    name = "game of thrones",
                    posterUrl = "",
                    releaseStatus = "2014 - Ongoing",
                    trackingStatus = "currently watching",
                    description = "game of thrones is a show about society",
                    seasonsInfo = "2 seasons - 16 episodes - 0h40m each",
                    castFirst = DetailsUiModel.Cast("William Dicksdoor 2 lines", "King Joffrey", ""),
                    castSecond = DetailsUiModel.Cast("Peter O'mo", "Joffrey's brother", ""),
                    watchProviders = listOf(
                        DetailsUiModel.WatchProvider("", ""),
                        DetailsUiModel.WatchProvider("", ""),
                        DetailsUiModel.WatchProvider("", ""),
                    ),
                    seasons = listOf(
                        DetailsUiModel.Season(
                            1, 1, "", false, true,
                            listOf(
                                DetailsUiModel.Season.Episode(
                                    1, "ep 1", "1", "name", "date", false, true,
                                ),
                                DetailsUiModel.Season.Episode(
                                    1, "ep 1", "1", "name", "date", false, true,
                                ),
                                DetailsUiModel.Season.Episode(
                                    1, "ep 1", "1", "name", "date", false, true,
                                ),
                            ),
                        ),
                        DetailsUiModel.Season(
                            1, 1, "", false, true,
                            listOf(
                                DetailsUiModel.Season.Episode(
                                    1, "ep 1", "1", "name", "date", true, true,
                                ),
                                DetailsUiModel.Season.Episode(
                                    1,
                                    "",
                                    "1",
                                    "ep 1123 12 3123123123 3231323 3 3dasd asd asdasdasd 2",
                                    "date",
                                    true,
                                    true
                                ),
                                DetailsUiModel.Season.Episode(
                                    1, "ep 1", "1", "name", "date", true, true,
                                ),
                            ),
                        )
                    )
                ),
                { }
            )
        }
    }
}
