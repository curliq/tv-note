package com.free.tvtracker.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.free.tvtracker.core.theme.TvTrackerTheme

@Composable
fun WatchlistScreen() {
    TvTrackerTheme {
        Box(Modifier.fillMaxSize().background(Color.Cyan)) {
            Text("Greeting().greet()")
            LazyColumn {

            }
        }
    }
}
