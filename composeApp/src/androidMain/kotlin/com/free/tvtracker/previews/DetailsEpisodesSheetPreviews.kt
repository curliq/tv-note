package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.previews.DetailsScreenPreviews.showDetailsUiModel
import com.free.tvtracker.screens.details.dialogs.DetailsEpisodesContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1200)
@Composable
fun DetailsEpisodesSheetPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsEpisodesContent(
                showDetailsUiModel,
                { }
            )
        }
    }
}
