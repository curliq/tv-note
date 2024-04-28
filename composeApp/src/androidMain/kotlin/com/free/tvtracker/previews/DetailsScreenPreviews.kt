package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.screens.details.DetailsScreenContent
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.core.theme.TvTrackerTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1200)
@Composable
fun DetailsScreenPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsScreenContent(
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
                    seasons = listOf()
                ),
                {}
            )
        }
    }
}
