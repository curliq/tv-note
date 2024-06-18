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
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
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
    }

    TvTrackerTheme {
        Scaffold(modifier.fillMaxWidth()) {
            DockedSearchBar(
                query = query,
                onQueryChange = { v -> viewModel.setSearchQuery(v) },
                onSearch = { viewModel.searchRefresh() },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search") },
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
                modifier = Modifier.fillMaxWidth().padding(16.dp).focusRequester(focusRequester)
            )
            when (results) {
                AddTrackedUiState.Error -> ErrorScreen(
                    Modifier.padding(top = 80.dp),
                    "Search again"
                ) { viewModel.searchRefresh() }

                is AddTrackedUiState.Ok -> AddTrackedScreenGrid(
                    results.data,
                    originScreen,
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
    results: List<AddTrackedItemUiModel>,
    originScreen: AddTrackedScreenOriginScreen,
    action: (AddTrackedViewModel.Action) -> Unit,
    navActions: (AddTrackedScreenNavAction) -> Unit,
) {
    val trackText = if (originScreen == AddTrackedScreenOriginScreen.Watchlist) "Watchlist" else "Track"
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(start = sidePadding, top = 86.dp, end = sidePadding, bottom = sidePadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results, key = { item -> item.tmdbId }) { item ->
            OutlinedCard(
                modifier = Modifier.fillMaxHeight().animateItemPlacement(),
                onClick = { navActions(AddTrackedScreenNavAction.GoContentDetails(item.tmdbId)) },
            ) {
                Box(Modifier.aspectRatio(posterRatio())) {
                    TvImage(item.image, modifier = Modifier.fillMaxHeight().fillMaxWidth())
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.padding(8.dp)) {
                    Text(
                        item.title,
                        minLines = 2,
                        maxLines = 2,
                        overflow = Ellipsis,
                        style = MaterialTheme.typography.labelMedium
                    )
                    if (originScreen == AddTrackedScreenOriginScreen.Discover) return@Column
                    Spacer(Modifier.height(16.dp))
                    val tracked = item.tracked
                    Row(modifier = Modifier.height(40.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = TvTrackerTheme.ShapeButton,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .height(40.dp)
                                .clip(TvTrackerTheme.ShapeButton)
                                .clickable(enabled = !tracked) {
                                    action(AddTrackedViewModel.Action.AddToTracked(item.tmdbId, originScreen))
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
            }
            Spacer(modifier = Modifier.height(8.dp))
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
