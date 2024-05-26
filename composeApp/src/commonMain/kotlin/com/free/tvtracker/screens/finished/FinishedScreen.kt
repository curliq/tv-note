package com.free.tvtracker.screens.finished

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.ScreenContentAnimation
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.screens.watching.FabContainer

sealed class FinishedScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int) : FinishedScreenNavAction()
    data object GoAddShow : FinishedScreenNavAction()
}

@Composable
fun FinishedScreen(navigate: (FinishedScreenNavAction) -> Unit, viewModel: FinishedShowsViewModel) {
    val shows = viewModel.shows.collectAsState().value
    TvTrackerTheme {
        FabContainer({ navigate(FinishedScreenNavAction.GoAddShow) }) {
            AnimatedContent(
                shows,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    FinishedUiState.Empty -> FinishedEmpty()
                    FinishedUiState.Error -> ErrorScreen { viewModel.refresh() }
                    FinishedUiState.Loading -> LoadingScreen()
                    is FinishedUiState.Ok -> FinishedOk(targetState, navigate)
                }
            }
        }
    }
}

@Composable
private fun FinishedEmpty() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Haven't finished any show yet.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FinishedOk(shows: FinishedUiState.Ok, navigate: (FinishedScreenNavAction) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = TvTrackerTheme.sidePadding)
    ) {
        items(shows.shows) { model ->
            WatchingItem(model) { navigate(FinishedScreenNavAction.GoShowDetails(model.tmdbId)) }
        }
    }
}

@Composable
fun WatchingItem(uiModel: FinishedShowUiModel, onClick: () -> Unit) {
    Card(
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
                Text(uiModel.status, style = TvTrackerTheme.Typography.bodyMedium)
            }
        }
    }
}