package com.free.tvtracker.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.previews.DiscoverPreviews.shows
import com.free.tvtracker.ui.discover.DiscoverOk
import com.free.tvtracker.ui.discover.DiscoverUiModel
import com.free.tvtracker.ui.discover.DiscoverUiState
import com.free.tvtracker.ui.discover.dialogs.DiscoverTrendingSheetContent

object DiscoverPreviews {
    val shows = DiscoverUiState.Ok(
        DiscoverUiModel(
            showsTrendingWeekly = listOf(
                DiscoverUiModel.Content(1, "game of throne", ""),
                DiscoverUiModel.Content(1, "game of throne", ""),
                DiscoverUiModel.Content(1, "game of throne", ""),
                DiscoverUiModel.Content(1, "game of throne", ""),
                DiscoverUiModel.Content(1, "game of throne throne theonr the", ""),
            ),
            showsReleasedSoon = listOf(
                DiscoverUiModel.Content(1, "game of throne", ""),
                DiscoverUiModel.Content(1, "game of throne", ""),
                DiscoverUiModel.Content(1, "game of throne", ""),
            ),
            showsRecommended = DiscoverUiModel.RecommendedContent(
                results = listOf(
                    DiscoverUiModel.Content(1, "game of throne", ""),
                    DiscoverUiModel.Content(1, "game of then theoh er throne", ""),
                    DiscoverUiModel.Content(1, "game of throne", ""),
                    DiscoverUiModel.Content(1, "game of throne", ""),
                ),
                selectionActive = listOf(
                    DiscoverUiModel.Content(1, "suits", ""),
                    DiscoverUiModel.Content(1, "avatar", ""),
                    DiscoverUiModel.Content(1, "haiju", ""),
                ),
                selectionActiveText = "suits, haiju, and avatar and game of throne",
                selectionAvailable = listOf(
                    DiscoverUiModel.RecommendedContent.Selection(1, "haijuhaijuhaijuhaijuhai", "", true),
                    DiscoverUiModel.RecommendedContent.Selection(
                        1,
                        "haiju, haijuhaiju, haiju, haiju, haiju",
                        "",
                        false
                    ),
                    DiscoverUiModel.RecommendedContent.Selection(1, "haijuhaiju,haijuhaiju", "", false),
                    DiscoverUiModel.RecommendedContent.Selection(1, "haiju", "", false),
                    DiscoverUiModel.RecommendedContent.Selection(1, "haiju", "", false),
                ),
                isLoading = false
            )
        ),
    )
}

@Preview
@Composable
fun DiscoverPreview() {
    MaterialTheme {
        Surface {
            DiscoverOk(shows, {})
        }
    }
}

@Preview
@Composable
fun DiscoverTrendingSheetPreview() {
    MaterialTheme {
        Surface {
            DiscoverTrendingSheetContent(shows.uiModel.showsTrendingWeekly, {})
        }
    }
}
