package com.free.tvtracker.ui.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@Composable
fun LoadingCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().aspectRatio(1.6f)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingIndicator()
        }
    }
}

@Composable
fun LoadingScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        LoadingCard(modifier = Modifier.padding(TvTrackerTheme.sidePadding))
    }
}
