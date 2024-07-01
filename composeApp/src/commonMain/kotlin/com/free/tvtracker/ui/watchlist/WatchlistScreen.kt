package com.free.tvtracker.ui.watchlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_movie
import besttvtracker.composeapp.generated.resources.ic_tv
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.backdropRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.watching.FabContainer
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel.WatchlistedAction

sealed class WatchlistScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow: Boolean) : WatchlistScreenNavAction()
    data object GoAddShow : WatchlistScreenNavAction()
}

@Composable
fun WatchlistScreen(viewModel: WatchlistedShowsViewModel, navigate: (WatchlistScreenNavAction) -> Unit) {
    val shows = viewModel.shows.collectAsState().value
    TvTrackerTheme {
        FabContainer({ navigate(WatchlistScreenNavAction.GoAddShow) }, content = {
            AnimatedContent(
                shows,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    WatchlistUiState.Empty -> WatchlistEmpty()
                    WatchlistUiState.Error -> ErrorScreen { viewModel.refresh() }
                    WatchlistUiState.Loading -> LoadingScreen()
                    is WatchlistUiState.Ok -> WatchlistOk(targetState, viewModel::action, navigate)
                }
            }
        })
    }
}

@Composable
private fun WatchlistEmpty() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No content watchlisted.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WatchlistOk(
    data: WatchlistUiState.Ok,
    action: (WatchlistedAction) -> Unit,
    navigate: (WatchlistScreenNavAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = sidePadding),
        contentPadding = PaddingValues(bottom = sidePadding)
    ) {
        item {
            Row(Modifier.padding(top = 8.dp)) {
                InputChip(
                    selected = data.filterTvShows,
                    onClick = { action(WatchlistedAction.ToggleTvShows) },
                    label = { Text("Tv Shows") },
                    leadingIcon = { ResImage(Res.drawable.ic_tv, "tv") },
                    trailingIcon = { FilterCloseIcon(data.filterTvShows) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                InputChip(
                    selected = data.filterMovies,
                    onClick = { action(WatchlistedAction.ToggleMovies) },
                    label = { Text("Movies") },
                    leadingIcon = { ResImage(Res.drawable.ic_movie, "movies") },
                    trailingIcon = { FilterCloseIcon(data.filterMovies) }
                )
            }
        }
        item {
            if (data.shows.isEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                WatchlistEmpty()
            }
        }
        items(data.shows, key = { it.tmdbId }) { model ->
            WatchlistItem(
                model,
                { navigate(WatchlistScreenNavAction.GoShowDetails(model.tmdbId, model.isTvShow)) },
                Modifier.animateItem()
            )
        }
    }
}

@Composable
fun FilterCloseIcon(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(220, delayMillis = 220)) + expandHorizontally(),
        exit = fadeOut(animationSpec = tween(220)) + shrinkHorizontally(
            animationSpec = tween(
                220,
                delayMillis = 220
            )
        )
    ) {
        Icon(
            Icons.Rounded.Clear,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun WatchlistItem(uiModel: WatchlistShowUiModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row {
            Box(Modifier.aspectRatio(backdropRatio())) {
                TvImage(uiModel.image)
            }
            Column(Modifier.padding(16.dp)) {
                Text(uiModel.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        uiModel.status,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.weight(1f))
                    ResImage(if (uiModel.isTvShow) Res.drawable.ic_tv else Res.drawable.ic_movie, "type")
                }
            }
        }
    }
}
