package com.free.tvtracker.core.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.theme.TvTrackerTheme

@Composable
fun ErrorCard(refresh: (() -> Unit)? = null, refreshText: String? = null, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(),) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(TvTrackerTheme.defaultSidePadding),
        ) {
            Icon(Icons.Rounded.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Something went wrong", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("There's probably an issue on the server's side, please try again later.")
            Spacer(modifier = Modifier.height(24.dp))
            refresh?.let {
                Button(onClick = it) {
                    Text(refreshText ?: "Refresh")
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, refreshText: String? = null, refresh: (() -> Unit)? = null) {
    Box(modifier = modifier.fillMaxSize()) {
        ErrorCard(refresh, refreshText, Modifier.padding(TvTrackerTheme.defaultSidePadding))
    }
}
