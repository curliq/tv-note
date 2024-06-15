package com.free.tvtracker.previews

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.previews.PersonPreviews.personUiModel
import com.free.tvtracker.ui.person.dialogs.PersonShowsContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(heightDp = 1700)
@Composable
fun PersonShowsSheetPreview() {
    TvTrackerTheme {
        Scaffold { padding ->
            PersonShowsContent(
                personUiModel, { }
            )
        }
    }
}
