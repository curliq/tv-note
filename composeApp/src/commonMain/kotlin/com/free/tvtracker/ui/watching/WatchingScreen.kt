package com.free.tvtracker.ui.watching

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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

sealed class WatchingScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow:Boolean) : WatchingScreenNavAction()
    data object GoAddShow : WatchingScreenNavAction()
}

@Composable
fun WatchingScreen(navigate: (WatchingScreenNavAction) -> Unit, viewModel: WatchingViewModel) {
    val shows = viewModel.shows.collectAsState().value
    TvTrackerTheme {
        FabContainer({ navigate(WatchingScreenNavAction.GoAddShow) }, content = {
            AnimatedContent(
                shows,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    is WatchingUiState.Ok -> WatchingOk(navigate, viewModel::markEpisodeWatched, targetState)
                    WatchingUiState.Error -> ErrorScreen { viewModel.refresh() }
                    WatchingUiState.Loading -> LoadingScreen()
                    WatchingUiState.Empty -> WatchingEmpty()
                }
            }
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchingOk(
    navigate: (WatchingScreenNavAction) -> Unit,
    markWatched: (Int?, Int?) -> Unit,
    shows: WatchingUiState.Ok
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(vertical = TvTrackerTheme.sidePadding)
    ) {
        if (shows.watching.isEmpty()) {
            item {
                Box(
                    Modifier.padding(start = TvTrackerTheme.sidePadding, bottom = 32.dp)
                ) {
                    Text(
                        text = "Nothing to watch. :(",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        itemsIndexed(
            shows.watching,
            key = { _, item -> item.tmdbId }
        ) { index, show ->
            WatchingItem(
                show,
                onClick = { navigate(WatchingScreenNavAction.GoShowDetails(show.tmdbId, true)) },
                onMarkWatched = markWatched,
                isWatchable = true
            )
        }
        if (shows.waitingNextEpisode.isNotEmpty()) {
            item(key = -1) {
                Column(Modifier.animateItemPlacement()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Waiting for next episode",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            itemsIndexed(
                shows.waitingNextEpisode,
                key = { _, item -> item.tmdbId }
            ) { index, show ->
                WatchingItem(
                    show,
                    onClick = { navigate(WatchingScreenNavAction.GoShowDetails(show.tmdbId, true)) },
                    onMarkWatched = markWatched,
                    isWatchable = false,
                )
            }
        }
    }
}

@Composable
fun WatchingEmpty() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Not tracking any show yet.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FabContainer(
    navigate: () -> Unit,
    icon: ImageVector = Icons.Default.Add,
    largeFab: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            if (largeFab) {
                LargeFloatingActionButton(
                    onClick = { navigate() },
                ) {
                    Icon(icon, "")
                }
            } else {
                FloatingActionButton(
                    onClick = { navigate() },
                ) {
                    Icon(icon, "")
                }
            }
        },
        content = content
    )
}

@Composable
fun LazyItemScope.WatchingItem(
    uiModel: WatchingItemUiModel,
    onClick: () -> Unit,
    onMarkWatched: (Int?, Int?) -> Unit,
    isWatchable: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
            .animateItem(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row {
            Box(Modifier.aspectRatio(posterRatio())) {
                TvImage(uiModel.image)
            }
            Column(Modifier.padding(16.dp)) {
                Text(uiModel.title, maxLines = 1)
                Spacer(Modifier.height(8.dp))
                if (uiModel.nextEpisode != null) {
                    WatchingItemNextEpisode(uiModel.nextEpisode)
                }
                Spacer(Modifier.height(24.dp))
                if (isWatchable) {
                    FilledTonalButton(
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                        shape = TvTrackerTheme.ShapeButton,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        onClick = { onMarkWatched(uiModel.trackedShowId, uiModel.nextEpisode?.id) },
                    ) {
                        Text(
                            text = "Mark episode as watched",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Text(
                        text = "Available on ${uiModel.nextEpisodeCountdown}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun WatchingItemNextEpisode(nextEpisode: WatchingItemUiModel.NextEpisode) {
    Row {
        val style = MaterialTheme.typography.bodyMedium
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
