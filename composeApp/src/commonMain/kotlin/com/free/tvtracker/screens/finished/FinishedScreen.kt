package com.free.tvtracker.screens.finished

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.TvTrackerTheme

@Composable
fun FinishedScreen(viewModel: FinishedShowsViewModel) {
    val shows = viewModel.shows.collectAsState().value
    TvTrackerTheme {
        when (shows) {
            FinishedUiState.Empty -> {}
            FinishedUiState.Error -> ErrorScreen { viewModel.refresh() }
            FinishedUiState.Loading -> LoadingScreen()
            is FinishedUiState.Ok -> {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 8.dp)) {
                    items(shows.shows) { model ->
                        WatchingItem(model) { }
                    }
                }
            }
        }

    }
}

@Composable
fun WatchingItem(uiModel: FinishedShowUiModel, onClick: () -> Unit) {
    OutlinedCard(
        Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row {
            Box(Modifier.aspectRatio(posterRatio())) {
                TvImage(uiModel.image)
            }
            Column(Modifier.padding(16.dp)) {
                Text(uiModel.title, maxLines = 1)
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiModel.status)
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiModel.nextEpisode ?: "")
            }
        }
    }
}
