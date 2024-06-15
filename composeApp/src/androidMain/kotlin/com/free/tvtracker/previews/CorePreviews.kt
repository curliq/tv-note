package com.free.tvtracker.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.theme.TvTrackerTheme


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

