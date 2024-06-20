package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.welcome.WelcomeContent
import com.free.tvtracker.ui.welcome.WelcomeViewModel

@Preview
@Composable
fun WelcomePreview() {
    TvTrackerTheme {
        Scaffold { a ->
            WelcomeContent(status = WelcomeViewModel.Status.GreenLight, actionOk = {})
        }
    }
}
