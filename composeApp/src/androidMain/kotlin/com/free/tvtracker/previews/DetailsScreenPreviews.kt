package com.free.tvtracker.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.screens.details.DetailsScreenContent
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.core.theme.TvTrackerTheme

@Preview
@Composable
fun DetailsScreenPreview() {
    TvTrackerTheme {
        DetailsScreenContent(
            DetailsUiModel(
                1,
                "game of thrones",
                "",
                "ongoing",
                "2014",
                listOf(DetailsUiModel.WhereToWatch("netflix", "")),
                "game of thrones is a show about society"
            )
        )
    }
}
