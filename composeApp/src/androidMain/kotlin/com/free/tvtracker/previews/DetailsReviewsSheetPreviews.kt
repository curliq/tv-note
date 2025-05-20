package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.previews.DetailsScreenPreviews.showDetailsUiModel
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.dialogs.DetailsReviewsSheetContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1000)
@Composable
fun DetailsReviewsSheetPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            DetailsReviewsSheetContent(
                showDetailsUiModel
            )
        }
    }
}
