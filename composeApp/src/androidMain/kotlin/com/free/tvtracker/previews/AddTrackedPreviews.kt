package com.free.tvtracker.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.search.AddTrackedItemUiModel
import com.free.tvtracker.ui.search.AddTrackedScreenGrid

@Preview
@Composable
fun AddTrackedShowGridPreview() {
    val shows = listOf(
        AddTrackedItemUiModel(1, "1", "game of thrones", "", true, AddTrackedItemUiModel.TrackAction.Watching),
        AddTrackedItemUiModel(2, "2", "game 2", "", true, AddTrackedItemUiModel.TrackAction.Watching),
        AddTrackedItemUiModel(3, "3", "game of thrones 3", "", false, AddTrackedItemUiModel.TrackAction.Watching),
        AddTrackedItemUiModel(
            4,
            "4",
            "game of thrones thrones thrones thrones 4",
            "",
            false,
            AddTrackedItemUiModel.TrackAction.Watching
        ),
        AddTrackedItemUiModel(5, "5", "game of  4", "", true, AddTrackedItemUiModel.TrackAction.Watching),
    )
    MaterialTheme {
        Scaffold { p ->
            AddTrackedScreenGrid(16.dp, shows, {}, {})
        }
    }
}
