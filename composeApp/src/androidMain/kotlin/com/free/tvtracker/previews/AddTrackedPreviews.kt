package com.free.tvtracker.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.search.AddTrackedItemUiModel
import com.free.tvtracker.ui.search.AddTrackedScreenGrid
import com.free.tvtracker.ui.search.AddTrackedScreenOriginScreen

@Preview
@Composable
fun AddTrackedShowGridPreview() {
    val shows = listOf(
        AddTrackedItemUiModel(1, "game of thrones", "", true),
        AddTrackedItemUiModel(2, "game 2", "", true),
        AddTrackedItemUiModel(3, "game of thrones 3", "", false),
        AddTrackedItemUiModel(4, "game of thrones thrones thrones thrones 4", "", false),
        AddTrackedItemUiModel(5, "game of  4", "", true),
    )
    MaterialTheme {
        AddTrackedScreenGrid(shows, AddTrackedScreenOriginScreen.Watchlist, {}, {})
    }
}
