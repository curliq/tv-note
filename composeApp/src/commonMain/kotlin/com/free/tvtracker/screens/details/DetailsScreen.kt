package com.free.tvtracker.screens.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.theme.ScreenContentAnimation
import com.free.tvtracker.core.theme.TvTrackerTheme

@Composable
fun DetailsScreen(viewModel: DetailsViewModel, showId: Int) {
    TvTrackerTheme {
        Box(Modifier.fillMaxSize()) {
            LaunchedEffect(viewModel) {
                viewModel.setId(showId)
            }
            val show = viewModel.result.collectAsState().value
            AnimatedContent(show, transitionSpec = ScreenContentAnimation()) { targetState ->
                when (targetState) {
                    DetailsUiState.Error -> ErrorScreen { viewModel.setId(showId) }
                    DetailsUiState.Loading -> LoadingScreen()
                    is DetailsUiState.Ok -> DetailsScreenContent(targetState.data)
                }
            }
        }
    }
}

@Composable
fun DetailsScreenContent(show: DetailsUiModel) {
    Column {
        OutlinedCard {
            Box(Modifier.width(100.dp).aspectRatio(1 / 1.5f).clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))) {
                TvImage(show.posterUrl, Modifier.fillMaxWidth())
            }
        }
        Text(text = show.name)
        Row {
            Button(onClick = {}) {
                Text("watching")
            }
            Button(onClick = {}) {
                Text("watched")
            }
            Button(onClick = {}) {
                Text("watchlist")
            }
        }
        Text(text = show.status)
        Text(text = show.releaseYear ?: "no release year")
        Text(text = show.description ?: "no description")
        Text(text = "seasons")
        show.whereToWatch.forEach {
            Row {
                Box(Modifier.size(50.dp).clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))) {
                    TvImage(it.platformLogo, Modifier.fillMaxHeight().fillMaxWidth())
                }
                Text(text = it.platformName)
            }
        }
    }
}
