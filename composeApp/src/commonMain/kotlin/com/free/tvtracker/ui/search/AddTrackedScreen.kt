package com.free.tvtracker.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults.InputFieldHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding

sealed class AddTrackedScreenNavAction {
    data class GoContentDetails(val showTmdbId: Int) : AddTrackedScreenNavAction()
}

enum class AddTrackedScreenOriginScreen { Watching, Finished, Watchlist, Discover }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackedScreen(
    viewModel: AddTrackedViewModel,
    navActions: (AddTrackedScreenNavAction) -> Unit,
    originScreen: AddTrackedScreenOriginScreen,
    modifier: Modifier = Modifier
) {
    val query = viewModel.searchQuery.collectAsState().value
    val results = viewModel.results.collectAsState().value
    val focusRequester = remember { FocusRequester() }

    if (viewModel.focusSearch.collectAsState().value) {
        viewModel.clearFocus()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.setOriginScreen(originScreen)
    }

    TvTrackerTheme {
        Scaffold(modifier.fillMaxWidth()) {
            DockedSearchBar(
                query = query,
                onQueryChange = { v -> viewModel.setSearchQuery(v) },
                onSearch = { viewModel.searchRefresh() },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search Tv shows, movies, or people") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedContent(transitionSpec = {
                        fadeIn(animationSpec = tween(50)) togetherWith fadeOut(animationSpec = tween(50))
                    }, targetState = results) { targetState ->
                        if (targetState is AddTrackedUiState.Ok && targetState.isSearching ||
                            targetState is AddTrackedUiState.Empty && targetState.isSearching
                        ) {
                            LoadingIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable { viewModel.setSearchQuery("") })
                        }
                    }
                },
                content = {},
                modifier = Modifier.fillMaxWidth().padding(sidePadding).focusRequester(focusRequester)
            )
            val searchbarHeight = InputFieldHeight + sidePadding * 2
            when (results) {
                AddTrackedUiState.Error -> ErrorScreen(
                    modifier = Modifier.padding(top = searchbarHeight),
                    "Search again"
                ) { viewModel.searchRefresh() }

                is AddTrackedUiState.Ok -> AddTrackedScreenGrid(
                    contentPaddingTop = searchbarHeight,
                    results.data,
                    viewModel::action,
                    navActions
                )

                is AddTrackedUiState.Empty -> EmptyView()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddTrackedScreenGrid(
    contentPaddingTop: Dp,
    results: List<AddTrackedItemUiModel>,
    action: (AddTrackedViewModel.Action) -> Unit,
    navActions: (AddTrackedScreenNavAction) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(
            start = sidePadding,
            top = contentPaddingTop,
            end = sidePadding,
            bottom = sidePadding
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(results, key = { i, item -> item.tmdbId }) { index, item ->
            OutlinedCard(
                modifier = Modifier.fillMaxHeight().animateItemPlacement(),
                onClick = { navActions(AddTrackedScreenNavAction.GoContentDetails(item.tmdbId)) },
            ) {
                Box(Modifier.aspectRatio(posterRatio())) {
                    TvImage(item.image, modifier = Modifier.fillMaxHeight().fillMaxWidth())
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.padding(8.dp)) {
                    val row = getRowIndexes(index)!!
                    val neighbour1 = results.getOrNull(row.first)
                    val neighbour2 = results.getOrNull(row.second)
                    // 16 characters is about the max to fit in one line on my emulator
                    val maxLines =
                        if (item.title.length < 16 &&
                            (neighbour1?.title?.length ?: 0) < 16 &&
                            (neighbour2?.title?.length ?: 0) < 16
                        ) 1 else 2
                    Text(
                        item.title,
                        minLines = maxLines,
                        maxLines = maxLines,
                        overflow = Ellipsis,
                        style = MaterialTheme.typography.labelMedium
                    )
                    val rowHasNoButtons =
                        neighbour1?.action == AddTrackedItemUiModel.TrackAction.None &&
                            neighbour2?.action == AddTrackedItemUiModel.TrackAction.None &&
                            item.action == AddTrackedItemUiModel.TrackAction.None
                    if (rowHasNoButtons) return@Column
                    // If at least 1 item in this row has action, then build action with alpha=0 to fill up the space
                    // so that all items have the same height
                    TrackAction(item, action, invisible = item.action == AddTrackedItemUiModel.TrackAction.None)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun getRowIndexes(forIndex: Int): Pair<Int, Int>? {
    when (forIndex.mod(3)) {
        0 -> return Pair(forIndex + 1, forIndex + 2)
        1 -> return Pair(forIndex - 1, forIndex + 1)
        2 -> return Pair(forIndex - 2, forIndex - 1)
    }
    return null
}

@Composable
private fun TrackAction(
    item: AddTrackedItemUiModel,
    action: (AddTrackedViewModel.Action) -> Unit,
    invisible: Boolean,
) {
    val trackText = when (item.action) {
        AddTrackedItemUiModel.TrackAction.Watching -> "Track"
        is AddTrackedItemUiModel.TrackAction.Watchlist -> "Watchlist"
        is AddTrackedItemUiModel.TrackAction.Finished -> "Track"
        AddTrackedItemUiModel.TrackAction.None -> ""
    }
    Spacer(Modifier.height(16.dp))
    val tracked = item.tracked
    Row(
        modifier = Modifier.height(40.dp).alpha(if (invisible) 0f else 1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = TvTrackerTheme.ShapeButton,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .height(40.dp)
                .clip(TvTrackerTheme.ShapeButton)
                .clickable(enabled = !tracked && !invisible) {
                    action(AddTrackedViewModel.Action.AddToTracked(item.tmdbId, item.action))
                }
        ) {
            Box(
                Modifier.animateContentSize(
                    animationSpec = tween(
                        durationMillis = 220,
                        easing = LinearOutSlowInEasing
                    )
                )
                    .widthIn(min = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                this@Row.AnimatedVisibility(
                    visible = tracked,
                    enter = fadeIn(animationSpec = tween(220, delayMillis = 220))
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        "",
                        modifier = Modifier.size(16.dp).align(Alignment.Center),
                        tint = Color.Black,
                    )
                }
                this@Row.AnimatedVisibility(
                    visible = !tracked,
                    exit = fadeOut(animationSpec = tween(220))
                ) {
                    Text(
                        trackText,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp).align(Alignment.Center),
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = tracked,
            enter = fadeIn(animationSpec = tween(220, delayMillis = 440))
        ) {
            Text(
                "Tracked",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF999999),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun EmptyView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No results found.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}
