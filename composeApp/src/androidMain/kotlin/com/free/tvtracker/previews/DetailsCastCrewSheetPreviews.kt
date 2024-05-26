package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.previews.DetailsScreenPreviews.showDetailsUiModel
import com.free.tvtracker.screens.details.dialogs.DetailsCastCrewContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1700)
@Composable
fun DetailsCastCrewSheetPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsCastCrewContent(
                showDetailsUiModel, { }
            )
        }
    }
}
