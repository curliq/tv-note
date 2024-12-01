package com.free.tvtracker.ui.finished

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_movie
import besttvtracker.composeapp.generated.resources.ic_tv
import com.free.tvtracker.domain.PurchaseStatus
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.watching.FabContainer
import com.free.tvtracker.ui.watching.TrialView
import com.free.tvtracker.ui.watchlist.FilterCloseIcon
import com.free.tvtracker.ui.watchlist.WatchlistItem
import com.free.tvtracker.ui.watchlist.calculateWatchlistItemHeight

sealed class FinishedScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow: Boolean) : FinishedScreenNavAction()
    data object GoAddShow : FinishedScreenNavAction()
}

@Composable
fun FinishedScreen(navigate: (FinishedScreenNavAction) -> Unit, viewModel: FinishedShowsViewModel) {
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    val shows = viewModel.shows.collectAsState().value
    val purchaseStatus by viewModel.status.collectAsState(PurchaseStatus(PurchaseStatus.Status.Purchased, null))

    TvTrackerTheme {
        FabContainer({ navigate(FinishedScreenNavAction.GoAddShow) }, content = {
            AnimatedContent(
                shows,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    FinishedUiState.Empty -> FinishedEmpty(
                        purchaseStatus,
                        { viewModel.action(FinishedShowsViewModel.FinishedAction.Buy) }
                    )

                    FinishedUiState.Error -> ErrorScreen { viewModel.refresh() }
                    FinishedUiState.Loading -> LoadingScreen()
                    is FinishedUiState.Ok -> FinishedOk(targetState, purchaseStatus, viewModel::action, navigate)
                }
            }
        })
    }
}

@Composable
fun FinishedEmpty(status: PurchaseStatus, onBuy: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No finished content.",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
            if (status.status != PurchaseStatus.Status.Purchased) {
                TrialView(status, onBuy)
            }
        }
    }
}

@Composable
fun FinishedOk(
    data: FinishedUiState.Ok,
    purchaseStatus: PurchaseStatus,
    action: (FinishedShowsViewModel.FinishedAction) -> Unit,
    navigate: (FinishedScreenNavAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = sidePadding),
        contentPadding = PaddingValues(bottom = sidePadding)
    ) {
        item {
            Row(Modifier.padding(top = 8.dp)) {
                InputChip(
                    selected = data.filterTvShows,
                    onClick = { action(FinishedShowsViewModel.FinishedAction.ToggleTvShows) },
                    label = { Text("Tv Shows") },
                    leadingIcon = { ResImage(Res.drawable.ic_tv, "tv", tint = MaterialTheme.colorScheme.onBackground) },
                    trailingIcon = { FilterCloseIcon(data.filterTvShows) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                InputChip(
                    selected = !data.filterTvShows,
                    onClick = { action(FinishedShowsViewModel.FinishedAction.ToggleMovies) },
                    label = { Text("Movies") },
                    leadingIcon = {
                        ResImage(
                            Res.drawable.ic_movie,
                            "movies",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    trailingIcon = { FilterCloseIcon(!data.filterTvShows) }
                )
            }
        }
        items(data.shows) { model ->
            WatchlistItem(
                model.toWatchlistUiModel(),
                onClick = { navigate(FinishedScreenNavAction.GoShowDetails(model.tmdbId, model.isTvShow)) },
                Modifier.animateItem().height(calculateWatchlistItemHeight())
            )
        }
        if (purchaseStatus.status != PurchaseStatus.Status.Purchased) {
            item(key = -1) {
                TrialView(purchaseStatus, { action(FinishedShowsViewModel.FinishedAction.Buy) })
            }
        }
    }
}
