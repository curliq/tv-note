package com.free.tvtracker.screens.finished

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.free.tvtracker.core.theme.TvTrackerTheme

@Composable
fun FinishedScreen() {
    TvTrackerTheme {
        Column(Modifier.fillMaxSize().background(Color.Green), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
            }) {
                Text("Click me!")
            }
        }
    }
}
