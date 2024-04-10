package com.free.tvtracker.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.theme.TvTrackerTheme

@Composable
fun DiscoverScreen() {
    TvTrackerTheme {
        Column(Modifier.fillMaxSize().background(Color.Green), horizontalAlignment = Alignment.CenterHorizontally) {
            LazyColumn {
                items(100) {
                    Text("hello from compose")
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
