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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.Logger
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.expect.OsPlatform
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import kotlinx.coroutines.flow.collectLatest

sealed class WatchingScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow: Boolean) : WatchingScreenNavAction()
    data object GoAddShow : WatchingScreenNavAction()
}

@Composable
fun WatchingScreen(navigate: (WatchingScreenNavAction) -> Unit, viewModel: WatchingViewModel) {
    val logger: Logger = viewModel.logger
    val shows = viewModel.shows.collectAsState().value
    logger.d("recomposing watching screen, shows: $shows", "WatchingScreen")
    val purchaseStatus by viewModel.status.collectAsState(PurchaseStatus(PurchaseStatus.Status.Purchased, "", "$2.99"))
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.toaster.collectLatest {
            it?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
    TvTrackerTheme {
        FabContainer(
            { navigate(WatchingScreenNavAction.GoAddShow) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = {
                AnimatedContent(
                    shows,
                    transitionSpec = ScreenContentAnimation(),
                    contentKey = { targetState -> targetState::class }
                ) { targetState ->
                    when (targetState) {
                        is WatchingUiState.Ok -> WatchingOk(
                            navigate,
                            viewModel::markEpisodeWatched,
                            targetState,
                            purchaseStatus,
                            viewModel::onBuy,
                            viewModel::onSub,
                            { logger.d(it, "WatchingScreen") }
                        )

                        WatchingUiState.Error -> ErrorScreen { viewModel.refresh() }
                        WatchingUiState.Loading -> LoadingScreen()
                        WatchingUiState.Empty -> WatchingEmpty(purchaseStatus, viewModel::onBuy, viewModel::onSub)
                    }
                }
            })
    }
}

@Composable
fun WatchingOk(
    navigate: (WatchingScreenNavAction) -> Unit,
    markWatched: (Int?, Int?) -> Unit,
    shows: WatchingUiState.Ok,
    purchaseStatus: PurchaseStatus,
    onBuy: () -> Unit,
    onSub: () -> Unit,
    log: (msg: String) -> Unit = { }
) {
    val watchingItemHeight: Dp = calculateWatchingItemHeight()
    log("recomposing watching OK screen, shows: ${shows.watching.map { it.tmdbId }}")
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(vertical = TvTrackerTheme.sidePadding)
    ) {
        if (shows.watching.isEmpty()) {
            item {
                Column(
                    Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nothing to watch. :(",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            itemsIndexed(
                shows.watching,
                key = { _, item -> item.trackedShowId }
            ) { index, show ->
                WatchingItem(
                    show,
                    onClick = { navigate(WatchingScreenNavAction.GoShowDetails(show.tmdbId, true)) },
                    onMarkWatched = markWatched,
                    isWatchable = true,
                    modifier = Modifier.height(watchingItemHeight)
                )
            }
        }
        if (shows.waitingNextEpisode.isNotEmpty()) {
            item(key = -1) {
                Column(Modifier.animateItem()) {
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
                key = { _, item -> item.trackedShowId }
            ) { index, show ->
                WatchingItem(
                    show,
                    onClick = { navigate(WatchingScreenNavAction.GoShowDetails(show.tmdbId, true)) },
                    onMarkWatched = markWatched,
                    isWatchable = false,
                    modifier = Modifier.height(watchingItemHeight)
                )
            }
        }
        if (purchaseStatus.status != PurchaseStatus.Status.Purchased) {
            item(key = -2) {
                TrialView(purchaseStatus, onBuy, onSub)
            }
        }
    }
}

@Composable
fun WatchingEmpty(status: PurchaseStatus, onBuy: () -> Unit, onSub: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Not tracking any show.",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
            if (status.status != PurchaseStatus.Status.Purchased) {
                TrialView(status, onBuy, onSub)
            }
        }
    }
}

@Composable
fun FabContainer(
    navigate: () -> Unit,
    icon: ImageVector = Icons.Rounded.Search,
    largeFab: Boolean = false,
    snackbarHost: @Composable () -> Unit = {},
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
        content = content,
        snackbarHost = snackbarHost
    )
}

/**
 * Calculates the height of a watchingitem before rendering them so that we can set the image height.
 * Needed because IntrinsicSize is not supported on iOS
 */
@Composable
private fun calculateWatchingItemHeight(): Dp {
    val textMeasurer = rememberTextMeasurer()
    val m1 = textMeasurer.measure(text = "A", maxLines = 1, style = MaterialTheme.typography.bodyLarge, softWrap = true)
    val height1 = with(LocalDensity.current) {
        m1.size.height.toDp()
    }
    val m2 = textMeasurer.measure(text = "A", style = MaterialTheme.typography.bodyMedium)
    val height2 = with(LocalDensity.current) {
        m2.size.height.toDp()
    }
    return 16.dp + // top margin
        height1 + // title
        8.dp + // spacer
        height2 + // next ep
        24.dp + // spacer
        32.dp + // mark watched button
        16.dp + // bottom margin
        8.dp + 8.dp // padding
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
                Text(
                    uiModel.title,
                    maxLines = 1,
                    // this removes font padding
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    )
                )
                Spacer(Modifier.height(8.dp))
                if (uiModel.nextEpisode != null) {
                    WatchingItemNextEpisode(uiModel.nextEpisode)
                }
                Spacer(Modifier.height(24.dp))
                if (isWatchable) {
                    FilledTonalButton(
                        modifier = Modifier.heightIn(min = 32.dp),
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                        shape = TvTrackerTheme.ShapeButton,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        onClick = { onMarkWatched(uiModel.trackedShowId, uiModel.nextEpisode?.id) },
                    ) {
                        Text(
                            text = "Mark episode as watched",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                                fontWeight = MaterialTheme.typography.labelMedium.fontWeight
                            ),
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

@Composable
fun TrialView(status: PurchaseStatus, onBuy: () -> Unit, onSub: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = TvTrackerTheme.sidePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val trialFinished = status.status == PurchaseStatus.Status.TrialFinished
        val addedShows = if (trialFinished) 1 else 0
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            "App in demo, $addedShows/1 shows tracked.",
            style = MaterialTheme.typography.labelLarge,
            color = if (trialFinished) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBuy, modifier = Modifier.fillMaxWidth(1f), shape = TvTrackerTheme.ShapeButton) {
            Text("Buy for ${status.price}")
        }
        Spacer(Modifier.height(8.dp))
        Text("Or", style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onSub, modifier = Modifier.fillMaxWidth(1f), shape = TvTrackerTheme.ShapeButton) {
            Text("Subscribe for ${status.subPrice}/month")
        }
        val hand = LocalUriHandler.current
        Row {
            Text("Includes 1 month trial.", style = MaterialTheme.typography.labelSmall)
            if (OsPlatform().get() == OsPlatform.Platform.IOS) {
                Text(
                    " Terms of service",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        hand.openUri("https://www.apple.com/legal/internet-services/itunes/dev/stdeula/")
                    }
                )
                Text(" &", style = MaterialTheme.typography.labelSmall)
                Text(
                    " privacy policy",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        hand.openUri("https://www.freeprivacypolicy.com/live/e43baeba-e657-4cfd-8eea-c7f12a64b78f")
                    }
                )
            }
        }
    }
}
