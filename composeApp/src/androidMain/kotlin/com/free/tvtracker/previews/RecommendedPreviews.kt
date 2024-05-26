package com.free.tvtracker.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.previews.DiscoverPreviews.shows
import com.free.tvtracker.screens.discover.RecommendedOk

@Preview(heightDp = 1000)
@Composable
fun RecommendedPreview() {
    MaterialTheme {
        Surface {
            RecommendedOk(shows, {}, {})
        }
    }
}
