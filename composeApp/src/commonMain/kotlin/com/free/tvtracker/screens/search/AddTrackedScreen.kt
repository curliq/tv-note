package com.free.tvtracker.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingIndicator
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.TvTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackedScreen(viewModel: AddTrackedViewModel) {
    val query = viewModel.searchQuery.collectAsState().value
    val results = viewModel.results.collectAsState().value
    TvTrackerTheme {
        Box(Modifier.fillMaxWidth()) {
            DockedSearchBar(
                query = query,
                onQueryChange = { v -> viewModel.setSearchQuery(v) },
                onSearch = { viewModel.searchRefresh() },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedContent(targetState = results) { targetState ->
                        if (targetState is AddTrackedUiState.Ok && targetState.isSearching) {
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
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            when (results) {
                AddTrackedUiState.Error -> ErrorScreen(
                    Modifier.padding(top = 80.dp),
                    "Search again"
                ) { viewModel.searchRefresh() }

                is AddTrackedUiState.Ok -> AddTrackedScreenGrid(results.data, viewModel::action)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddTrackedScreenGrid(results: List<AddTrackedItemUiModel>, action: (AddTrackedViewModel.Action) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(start = 12.dp, top = 86.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results, key = { item -> item.id }) { item ->
            OutlinedCard(Modifier.fillMaxHeight().animateItemPlacement()) {
                Box(Modifier.aspectRatio(posterRatio()).clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))) {
                    TvImage(item.image, Modifier.fillMaxHeight().fillMaxWidth())
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.padding(8.dp)) {
                    Text(
                        item.title,
                        minLines = 2,
                        maxLines = 2,
                        overflow = Ellipsis,
                        style = TvTrackerTheme.Typography.labelMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    val tracked = item.tracked
                    Row(modifier = Modifier.height(40.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(40.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .height(40.dp)
                                .clip(RoundedCornerShape(40.dp))
                                .clickable(enabled = !tracked) {
                                    action(AddTrackedViewModel.Action.AddToTracked(item.id))
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
                                        "Track",
                                        style = TvTrackerTheme.Typography.labelLarge,
                                        modifier = Modifier.padding(ButtonDefaults.ContentPadding)
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
