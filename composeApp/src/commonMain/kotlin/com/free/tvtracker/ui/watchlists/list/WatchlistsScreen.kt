package com.free.tvtracker.ui.watchlists.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.Logger
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.backdropRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.watching.FabContainer
import com.free.tvtracker.ui.watching.TrialView

sealed class WatchlistsScreenNavAction {
    data object GoAddShow : WatchlistsScreenNavAction()
    data class GoWatchlistDetails(val watchlistId: Int, val watchlistName: String) : WatchlistsScreenNavAction()
    data object HideBottomSheet : WatchlistsScreenNavAction()
}

@Composable
fun WatchlistsScreen(viewModel: WatchlistsViewModel, navigate: (WatchlistsScreenNavAction) -> Unit) {
    Logger().d("collect state", "WatchlistsScreen")
    val state = viewModel.stateAsFlow.collectAsState(WatchlistsUiState.Loading).value
    val purchaseStatus by viewModel.status.collectAsState(PurchaseStatus(PurchaseStatus.Status.Purchased, "", ""))
    LaunchedEffect(Unit) {
        Logger().d("fetch", "WatchlistsScreen")
        viewModel.fetch()
    }
    TvTrackerTheme {
        FabContainer({ navigate(WatchlistsScreenNavAction.GoAddShow) }, content = {
            AnimatedContent(
                state,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    WatchlistsUiState.Empty -> WatchlistEmpty(
                        purchaseStatus,
                        { viewModel.action(WatchlistsViewModel.WatchlistsAction.Buy) },
                        { viewModel.action(WatchlistsViewModel.WatchlistsAction.Sub) }
                    )

                    WatchlistsUiState.Error -> ErrorScreen { viewModel.fetch() }
                    WatchlistsUiState.Loading -> LoadingScreen()
                    is WatchlistsUiState.Ok -> WatchlistsOk(targetState, purchaseStatus, viewModel::action, navigate)
                }
            }
        })
    }
}

@Composable
fun WatchlistEmpty(status: PurchaseStatus, onBuy: () -> Unit, onSub: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No content watchlisted.",
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
fun WatchlistsOk(
    data: WatchlistsUiState.Ok,
    status: PurchaseStatus,
    action: (WatchlistsViewModel.WatchlistsAction) -> Unit,
    navigate: (WatchlistsScreenNavAction) -> Unit
) {
    Logger().d("WatchlistsOk: ${data.watchlists}", "WatchlistsScreen")
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = sidePadding),
        contentPadding = PaddingValues(bottom = sidePadding)
    ) {
        items(data.watchlists, key = { it.id }) { model ->
            WatchlistsItem(
                model,
                { navigate(WatchlistsScreenNavAction.GoWatchlistDetails(model.id, model.name)) },
                Modifier.animateItem().height(calculateWatchlistItemHeight())
            )
        }
        if (status.status != PurchaseStatus.Status.Purchased) {
            item(key = -1) {
                TrialView(
                    status,
                    { action(WatchlistsViewModel.WatchlistsAction.Buy) },
                    { action(WatchlistsViewModel.WatchlistsAction.Sub) }
                )
            }
        }
    }
}

/**
 * Calculates the height of a watchlistsitem before rendering them so that we can set the image height.
 * Needed because IntrinsicSize is not supported on iOS
 */
@Composable
fun calculateWatchlistItemHeight(): Dp {
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
        height2 + // status
        16.dp + // bottom margin
        8.dp + 8.dp // padding
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WatchlistsItem(uiModel: WatchlistsUiModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.aspectRatio(backdropRatio()),
            ) {
                items(4) {
                    val padding = when (it) {
                        0 -> PaddingValues(0.dp, 0.dp, 2.dp, 2.dp)
                        1 -> PaddingValues(2.dp, 0.dp, 0.dp, 2.dp)
                        2 -> PaddingValues(0.dp, 2.dp, 2.dp, 0.dp)
                        3 -> PaddingValues(2.dp, 2.dp, 0.dp, 0.dp)
                        else -> return@items
                    }
                    Box(Modifier.aspectRatio(backdropRatio()).padding(padding)) {
                        uiModel.thumbnails.getOrNull(it)?.let {
                            TvImage(it)
                        } ?: run {
                            TvImage("", error = { })
                        }
                    }
                }
            }
            Column(Modifier.padding(16.dp)) {
                Text(
                    uiModel.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${uiModel.tvShowCount} Tv Shows", style = MaterialTheme.typography.bodyMedium)
                    Box(Modifier.padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                        Box(
                            Modifier.size(4.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outlineVariant),
                        )
                    }
                    Text("${uiModel.movieCount} Movies", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
