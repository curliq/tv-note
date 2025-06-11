package com.free.tvtracker.ui.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_filter
import besttvtracker.composeapp.generated.resources.ic_movie
import besttvtracker.composeapp.generated.resources.ic_tv
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.SeeAllCard
import com.free.tvtracker.ui.watching.FabContainer
import com.free.tvtracker.ui.watchlists.details.FilterCloseIcon

sealed class DiscoverScreenNavActions {
    data object GoAddShow : DiscoverScreenNavActions()
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow: Boolean) : DiscoverScreenNavActions()
    data object GoRecommendations : DiscoverScreenNavActions()
    data object GoTrending : DiscoverScreenNavActions()
    data object GoNewRelease : DiscoverScreenNavActions()
}

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    navigate: (DiscoverScreenNavActions) -> Unit,
    paddingValues: PaddingValues = PaddingValues()
) {
    val data = viewModel.data.collectAsState().value
    LaunchedEffect(Unit) {
        if (data !is DiscoverUiState.Ok) { // prevents always loading on ios
            viewModel.refresh(showLoading = true)
        }
    }
    TvTrackerTheme {
        FabContainer(
            navigate = { navigate(DiscoverScreenNavActions.GoAddShow) },
            icon = Icons.Rounded.Search,
            content = {
                AnimatedContent(
                    data,
                    transitionSpec = ScreenContentAnimation(),
                    contentKey = { targetState -> targetState::class }
                ) { targetState ->
                    when (targetState) {
                        DiscoverUiState.Error -> ErrorScreen { viewModel.refresh(true) }
                        DiscoverUiState.Loading -> LoadingScreen()
                        is DiscoverUiState.Ok -> DiscoverOk(
                            targetState,
                            viewModel.filterTvShows.collectAsState().value,
                            viewModel::toggleContentFilter,
                            navigate
                        )
                    }
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DiscoverOk(
    data: DiscoverUiState.Ok,
    filterTvShows: Boolean,
    toggleFilterTvShows: (Boolean) -> Unit,
    navigate: (DiscoverScreenNavActions) -> Unit
) {
    // Column is not scrollable for some reason but LazyColumn is
    LazyColumn(Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
        item {
            Row(Modifier.padding(top = 8.dp)) {
                InputChip(
                    selected = filterTvShows,
                    onClick = { toggleFilterTvShows(!filterTvShows) },
                    label = { Text("Tv Shows") },
                    leadingIcon = { ResImage(Res.drawable.ic_tv, "tv", tint = MaterialTheme.colorScheme.onBackground) },
                    trailingIcon = { FilterCloseIcon(filterTvShows) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                InputChip(
                    selected = !filterTvShows,
                    onClick = { toggleFilterTvShows(!filterTvShows) },
                    label = { Text("Movies") },
                    leadingIcon = {
                        ResImage(
                            Res.drawable.ic_movie,
                            "movies",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    trailingIcon = { FilterCloseIcon(!filterTvShows) }
                )
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
            Text("Recommended", style = MaterialTheme.typography.titleLarge)
            if (data.uiModel.contentRecommended.selectionAvailable.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ResultsBasedOnText(
                        data.uiModel.contentRecommended.selectionActiveText,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    FilledTonalIconButton(
                        modifier = Modifier.widthIn(min = 64.dp),
                        shape = TvTrackerTheme.ShapeButton,
                        colors = IconButtonDefaults.filledTonalIconButtonColors()
                            .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        onClick = { navigate(DiscoverScreenNavActions.GoRecommendations) }
                    ) {
                        ResImage(
                            res = Res.drawable.ic_filter,
                            contentDescription = "Change filters",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            DiscoverRow(
                data.uiModel.contentRecommended.resultsPreview,
                navigate,
                seeAllAction = { navigate(DiscoverScreenNavActions.GoRecommendations) }
            )
            Spacer(Modifier.height(24.dp))

            Text("Trending this week", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            DiscoverRow(
                data.showsTrendingWeeklyPreview,
                navigate,
                seeAllAction = { navigate(DiscoverScreenNavActions.GoTrending) })
            Spacer(Modifier.height(24.dp))

            Text(
                if (filterTvShows) "New episode out soon" else "Upcoming releases",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            DiscoverRow(
                data.showsReleasedSoonPreview,
                navigate,
                seeAllAction = { navigate(DiscoverScreenNavActions.GoNewRelease) })
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ResultsBasedOnText(rec: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text("Based on: ", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = buildAnnotatedString {
                append(rec)
                if (rec.contains(" and ")) {
                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                        ),
                        start = rec.indexOf(" and "),
                        end = rec.indexOf(" and ") + 5
                    )
                }
                rec.forEachIndexed { index, c ->
                    if (c == ',') {
                        addStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                            ),
                            start = index,
                            end = index + 1
                        )
                    }
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            overflow = Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DiscoverRow(
    content: List<DiscoverUiModel.Content>,
    navAction: (DiscoverScreenNavActions) -> Unit,
    seeAllAction: () -> Unit
) {
    if (content.isEmpty()) {
        Box(Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "No show available",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DiscoverRowItem(content.getOrNull(0) ?: DiscoverUiModel.Content(-1, "", "", true), navAction)
            DiscoverRowItem(content.getOrNull(1) ?: DiscoverUiModel.Content(-1, "", "", true), navAction)
            DiscoverRowItem(content.getOrNull(2) ?: DiscoverUiModel.Content(-1, "", "", true), navAction)
            Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                SeeAllCard { seeAllAction() }
            }
        }
    }
}

@Composable
private fun RowScope.DiscoverRowItem(
    movieOrShow: DiscoverUiModel.Content,
    navAction: (DiscoverScreenNavActions) -> Unit
) {
    Box(Modifier.fillMaxWidth().weight(0.8f / 3f)) { // 3 cards + see all with 20% width
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            onClick = {
                if (movieOrShow.tmdbId != -1) {
                    navAction(
                        DiscoverScreenNavActions.GoShowDetails(
                            movieOrShow.tmdbId,
                            movieOrShow.isTvShow
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.aspectRatio(posterRatio())) {
                TvImage(movieOrShow.image, modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.padding(8.dp)) {
                Text(
                    movieOrShow.title,
                    minLines = 1,
                    maxLines = 3,
                    overflow = Ellipsis,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
