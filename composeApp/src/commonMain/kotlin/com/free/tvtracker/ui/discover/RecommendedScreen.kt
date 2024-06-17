package com.free.tvtracker.ui.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.NonLazyGrid
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

sealed class RecommendedScreenNavActions {
    data class GoShowDetails(val tmdbShowId: Int) : RecommendedScreenNavActions()
}

@Composable
fun RecommendedScreen(
    viewModel: DiscoverViewModel,
    navigate: (RecommendedScreenNavActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val data = viewModel.uiModel.collectAsState().value
    TvTrackerTheme {
        Scaffold(modifier.fillMaxSize()) {
            AnimatedContent(
                data,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    DiscoverUiState.Error -> ErrorScreen { viewModel.refresh() }
                    DiscoverUiState.Loading -> LoadingScreen()
                    is DiscoverUiState.Ok -> RecommendedOk(targetState, navigate, viewModel::action)
                }
            }
        }
    }
}

@Composable
fun RecommendedOk(
    data: DiscoverUiState.Ok,
    navigate: (RecommendedScreenNavActions) -> Unit,
    action: (DiscoverViewModel.DiscoverViewModelAction) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
        HasResults(data.uiModel.showsRecommended, navigate, action)
    }
}

@Composable
private fun HasResults(
    recommended: DiscoverUiModel.RecommendedContent,
    navigate: (RecommendedScreenNavActions) -> Unit,
    action: (DiscoverViewModel.DiscoverViewModelAction) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Spacer(Modifier.height(TvTrackerTheme.sidePadding))
//            Text("These recommendations are based on multiple factors such as genre, keywords, cast, crew, trends, and filtering techniques leveraging user generated data. Provided by TMDB")
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                Spacer(Modifier.height(TvTrackerTheme.sidePadding))
                Text(
                    "Select to filter:",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)
                )
                Spacer(Modifier.height(TvTrackerTheme.sidePadding))
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().height(320.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = TvTrackerTheme.sidePadding),
                ) {
                    items(recommended.selectionAvailable) { item ->
                        SelectionCard(content = item, action)
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(
                        start = TvTrackerTheme.sidePadding,
                        top = 24.dp,
                        end = TvTrackerTheme.sidePadding,
                        bottom = TvTrackerTheme.sidePadding
                    )
                ) {
                    FilledTonalButton(
                        onClick = { action(DiscoverViewModel.DiscoverViewModelAction.RecommendedUpdate) },
                        shape = TvTrackerTheme.ShapeButton,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (recommended.isLoading) {
                            LoadingIndicator(
                                modifier = Modifier.height(24.dp).aspectRatio(1f),
                            )
                        } else {
                            Text("Get results", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    FilledTonalButton(
                        onClick = { action(DiscoverViewModel.DiscoverViewModelAction.RecommendedSelectionClear) },
                        shape = TvTrackerTheme.ShapeButton,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Text("Clear selection (${recommended.selectionCountLive})")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Results", style = MaterialTheme.typography.titleLarge)
            ResultsBasedOnText(recommended.selectionActiveText)
            Spacer(Modifier.height(8.dp))
            if (recommended.results.isEmpty()) {
                Box(Modifier.padding(vertical = 16.dp)) {
                    Text(
                        text = "No show available",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            NonLazyGrid(3, recommended.results.size) { index ->
                val item = recommended.results[index]
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    onClick = { navigate(RecommendedScreenNavActions.GoShowDetails(item.tmdbId)) },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(Modifier.aspectRatio(posterRatio())) {
                        TvImage(item.image, modifier = Modifier.fillMaxSize())
                    }
                    Column(Modifier.padding(8.dp)) {
                        Text(
                            item.title,
                            minLines = 1,
                            maxLines = 3,
                            overflow = Ellipsis,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            Spacer(Modifier.height(TvTrackerTheme.sidePadding))
        }
    }
}

@Composable
private fun SelectionCard(
    content: DiscoverUiModel.RecommendedContent.Selection,
    action: (DiscoverViewModel.DiscoverViewModelAction) -> Unit,
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        onClick = { action(DiscoverViewModel.DiscoverViewModelAction.RecommendedSelectionAdded(content.tmdbId)) },
        // aspect ratio doesn't work if we set it on the TvImage container so we just set it here
        modifier = Modifier.aspectRatio(1f / 1.8f).then(if (content.isSelected) Modifier.alpha(0.5f) else Modifier)
    ) {
        Column(Modifier.width(140.dp)) {
            Box(Modifier.weight(1f)) {
                TvImage(content.image)
            }
            Column(Modifier.padding(8.dp).width(IntrinsicSize.Min)) {
                Text(
                    content.title,
                    minLines = 2,
                    maxLines = 2,
                    overflow = Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}
