package com.free.tvtracker.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.theme.TvTrackerTheme


@Preview
@Composable
fun LoadingScreenPreview() {
    TvTrackerTheme {
        LoadingScreen()
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    TvTrackerTheme {
        ErrorScreen()
    }
}

