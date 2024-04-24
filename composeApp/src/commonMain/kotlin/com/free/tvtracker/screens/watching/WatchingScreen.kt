package com.free.tvtracker.screens.watching

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.ScreenContentAnimation
import com.free.tvtracker.core.theme.TvTrackerTheme

sealed class NavAction {
    data class GoShowDetails(val showId: Int) : NavAction()
    data object GoAddShow : NavAction()
}

@Composable
fun WatchingScreen(
    navigate: (NavAction) -> Unit,
    viewModel: WatchingViewModel
) {
    val shows = viewModel.shows.collectAsState().value
    TvTrackerTheme {
        AnimatedContent(shows, transitionSpec = ScreenContentAnimation(), contentKey = { targetState ->
            if (targetState is WatchingUiState.Ok) {
                targetState.watching.size
            } else targetState
        }
        ) { targetState ->
            when (targetState) {
                is WatchingUiState.Ok -> WatchingOk(navigate, viewModel::markEpisodeWatched, targetState)
                WatchingUiState.Error -> ErrorScreen { viewModel.refresh() }
                WatchingUiState.Loading -> LoadingScreen()
                WatchingUiState.Empty -> WatchingEmpty(navigate)
            }
        }
    }
}

@Composable
fun WatchingOk(navigate: (NavAction) -> Unit, markWatched: (Int?, String?) -> Unit, shows: WatchingUiState.Ok) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigate(NavAction.GoAddShow) },
            ) {
                Icon(Icons.Default.Add, "")
            }
        },
    ) {
        Column {
            LazyColumn {
                itemsIndexed(
                    shows.watching
                ) { index, show ->
                    WatchingItem(
                        show,
                        onClick = { navigate(NavAction.GoShowDetails(show.tmdbId)) },
                        onMarkWatched = markWatched
                    )
                }
            }
            if (shows.waitingNextEpisode.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Waiting for next episode",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    itemsIndexed(
                        shows.waitingNextEpisode
                    ) { index, show ->
                        WatchingItem(
                            show,
                            onClick = { navigate(NavAction.GoShowDetails(show.tmdbId)) },
                            onMarkWatched = markWatched
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WatchingEmpty(navigate: (NavAction) -> Unit) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigate(NavAction.GoAddShow) },
            ) {
                Icon(Icons.Default.Add, "")
            }
        },
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Not tracking any show yet.",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WatchingItem(uiModel: WatchingItemUiModel, onClick: () -> Unit, onMarkWatched: (Int?, String?) -> Unit) {
    OutlinedCard(
        Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
    ) {
        Row {
            Box(
                Modifier
                    .aspectRatio(posterRatio())
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium)
                    )
                    .clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))
            ) {
                TvImage(uiModel.image)
            }
            Column(Modifier.padding(16.dp)) {
                Text(uiModel.title, maxLines = 1)
                Spacer(Modifier.height(8.dp))
                if (uiModel.nextEpisode != null) {
                    WatchingItemNextEpisode(uiModel.nextEpisode)
                }
                Spacer(Modifier.height(24.dp))
                FilledTonalButton(
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    onClick = { onMarkWatched(uiModel.trackedShowId, uiModel.nextEpisode?.id) },
                ) {
                    Text(text = "Mark episode as watched", style = TvTrackerTheme.Typography.labelMedium)
                }
            }
        }
    }
}

@Composable
private fun WatchingItemNextEpisode(nextEpisode: WatchingItemUiModel.NextEpisode) {
    Row {
        val style = TvTrackerTheme.Typography.bodyMedium
        Text(nextEpisode.body, style = style)
        AnimatedContent(
            targetState = nextEpisode,
            transitionSpec = {
                if (targetState.seasonNumber > initialState.seasonNumber) {
                    // season up
                    slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                } else {
                    EnterTransition.None togetherWith ExitTransition.None
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { targetCount ->
            Text(targetCount.season, style = style)
        }
        AnimatedContent(
            targetState = nextEpisode,
            transitionSpec = {
                if (targetState.episodeNumber > initialState.episodeNumber) {
                    // episode up
                    slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                } else {
                    // season up and episode back to 1
                    slideInVertically(tween(delayMillis = 100)) { height -> height } + fadeIn(
                        tween(delayMillis = 100)
                    ) togetherWith
                        slideOutVertically(tween(delayMillis = 100)) { height -> -height } + fadeOut(
                        tween(delayMillis = 100)
                    )
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { targetCount ->
            Text(targetCount.episode, style = style)
        }
    }
}
