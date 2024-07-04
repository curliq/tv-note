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
            contentTrendingWeekly = DiscoverUiModel.ContentPaged(
                listOf(
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne throne theonr the", "", true),
                ), 1, true
            ),
            contentReleasedSoon = DiscoverUiModel.ContentPaged(
                listOf(
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                ), 1, true
            ),
            contentRecommended = DiscoverUiModel.RecommendedContent(
                results = listOf(
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of then theoh er throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                    DiscoverUiModel.Content(1, "game of throne", "", true),
                ),
                selectionActive = listOf(
                    DiscoverUiModel.Content(1, "suits", "", true),
                    DiscoverUiModel.Content(1, "avatar", "", true),
                    DiscoverUiModel.Content(1, "haiju", "", true),
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
            DiscoverOk(shows, true, {}, {})
        }
    }
}

@Preview
@Composable
fun DiscoverTrendingSheetPreview() {
    MaterialTheme {
        Surface {
            DiscoverTrendingSheetContent(shows.uiModel.contentTrendingWeekly.data, {}, {})
        }
    }
}
