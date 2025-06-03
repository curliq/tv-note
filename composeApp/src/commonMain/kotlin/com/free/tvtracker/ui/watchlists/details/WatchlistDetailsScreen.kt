package com.free.tvtracker.ui.watchlists.details

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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

sealed class WatchlistDetailsScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow: Boolean) : WatchlistDetailsScreenNavAction()
    data object GoAddShow : WatchlistDetailsScreenNavAction()
    data class ShowRenameDialog(val tmdbShowId: Int, val watchlistName: String) : WatchlistDetailsScreenNavAction()
    data object HideRenameDialog : WatchlistDetailsScreenNavAction()
    data object OnDelete : WatchlistDetailsScreenNavAction()
}

@Composable
fun WatchlistDetailsScreen(
    viewModel: WatchlistDetailsViewModel,
    content: WatchlistDetailsViewModel.LoadContent,
    navigate: (WatchlistDetailsScreenNavAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shows = viewModel.shows.collectAsState().value

    TvTrackerTheme {
        Scaffold(modifier = modifier) {
            LaunchedEffect(content) {
                viewModel.loadContent(content.watchlistId, content.watchlistName)
            }
            FabContainer({ navigate(WatchlistDetailsScreenNavAction.GoAddShow) }, content = {
                AnimatedContent(
                    shows,
                    transitionSpec = ScreenContentAnimation(),
                    contentKey = { targetState -> targetState::class }
                ) { targetState ->
                    when (targetState) {
                        WatchlistDetailsUiState.Error -> ErrorScreen {
                            viewModel.loadContent(
                                content.watchlistId,
                                content.watchlistName
                            )
                        }

                        WatchlistDetailsUiState.Loading -> LoadingScreen()
                        is WatchlistDetailsUiState.Ok -> WatchlistOk(targetState, viewModel::action, navigate)
                    }
                }
            })
        }
    }
}

@Composable
fun WatchlistOk(
    data: WatchlistDetailsUiState.Ok,
    action: (WatchlistDetailsViewModel.WatchlistDetailsAction) -> Unit,
    navigate: (WatchlistDetailsScreenNavAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = sidePadding),
        contentPadding = PaddingValues(bottom = sidePadding)
    ) {
        item {
            Row(Modifier.padding(top = 8.dp)) {
                InputChip(
                    selected = data.filterTvShows,
                    onClick = { action(WatchlistDetailsViewModel.WatchlistDetailsAction.ToggleTvShows) },
                    label = { Text("Tv Shows") },
                    leadingIcon = { ResImage(Res.drawable.ic_tv, "tv", tint = MaterialTheme.colorScheme.onBackground) },
                    trailingIcon = { FilterCloseIcon(data.filterTvShows) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                InputChip(
                    selected = !data.filterTvShows,
                    onClick = { action(WatchlistDetailsViewModel.WatchlistDetailsAction.ToggleMovies) },
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
        if (data.shows.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                    Text(
                        text = "No content watchlisted.",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        items(data.shows, key = { it.tmdbId }) { model ->
            WatchlistItem(
                model,
                { navigate(WatchlistDetailsScreenNavAction.GoShowDetails(model.tmdbId, model.isTvShow)) },
                Modifier.animateItem().height(calculateWatchlistItemHeight())
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

/**
 * Calculates the height of a watchlistitem before rendering them so that we can set the image height.
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

@Composable
fun WatchlistItem(uiModel: WatchlistDetailsShowUiModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier
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
                Text(
                    uiModel.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiModel.status, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
