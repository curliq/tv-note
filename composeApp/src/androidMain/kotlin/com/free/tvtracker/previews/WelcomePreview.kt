package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.welcome.WelcomeContent
import com.free.tvtracker.ui.welcome.WelcomeViewModel

@Preview(showSystemUi = true)
@Composable
fun WelcomePreview() {
    TvTrackerTheme {
        Scaffold { a ->
            WelcomeContent(
                status = WelcomeViewModel.Status.GreenLight,
                actionOk = {},
                price = "£2.99",
                subPrice = "£0.99",
                buy = {},
                refresh = {},
                sub = {}
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomePreview2() {
    TvTrackerTheme {
        Scaffold { a ->
            WelcomeContent(
                status = WelcomeViewModel.Status.GreenLight,
                actionOk = {},
                price = "£2.99",
                subPrice = "£0.99",
                buy = {},
                refresh = {},
                sub = {}
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomePreview3() {
    TvTrackerTheme {
        Scaffold { a ->
            WelcomeContent(
                status = WelcomeViewModel.Status.GreenLight,
                actionOk = {},
                price = "£2.99",
                subPrice = "£0.99",
                buy = {},
                refresh = {},
                sub = {},
                pageIndex = 1
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomePreviewLoading() {
    TvTrackerTheme {
        Scaffold { a ->
            WelcomeContent(
                status = WelcomeViewModel.Status.LoadingPrice,
                actionOk = {},
                price = "£2.99",
                subPrice = "£0.99",
                buy = {},
                refresh = {},
                sub = {},
                pageIndex = 1
            )
        }
    }
}
