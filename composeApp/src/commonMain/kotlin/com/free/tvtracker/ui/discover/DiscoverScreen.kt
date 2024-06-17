package com.free.tvtracker.ui.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.SeeAllCard
import com.free.tvtracker.ui.watching.FabContainer
import org.jetbrains.compose.resources.ExperimentalResourceApi

sealed class DiscoverScreenNavActions {
    data object GoAddShow : DiscoverScreenNavActions()
    data class GoShowDetails(val tmdbShowId: Int) : DiscoverScreenNavActions()
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
    val data = viewModel.uiModel.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        (0..200).forEach {
            item {
                Text("test")
                Spacer(modifier = Modifier.height(8.dp))
            }
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
                        DiscoverUiState.Error -> ErrorScreen { viewModel.refresh() }
                        DiscoverUiState.Loading -> LoadingScreen()
                        is DiscoverUiState.Ok -> DiscoverOk(targetState, navigate)
                    }
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DiscoverOk(data: DiscoverUiState.Ok, navigate: (DiscoverScreenNavActions) -> Unit) {
    // Column is not scrollable for some reason but LazyColumn is
    LazyColumn(Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
        item {
            Spacer(Modifier.height(16.dp))
            Text("Recommended", style = MaterialTheme.typography.titleLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                ResultsBasedOnText(
                    data.uiModel.showsRecommended.selectionActiveText,
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
            DiscoverRow(
                data.uiModel.showsRecommended.resultsPreview,
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

            Text("New episode out soon", style = MaterialTheme.typography.titleLarge)
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
                if (rec.contains("and")) {
                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                        ),
                        start = rec.indexOf("and"),
                        end = rec.indexOf("and") + 3
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
            content.forEach { movieOrShow ->
                Box(Modifier.fillMaxWidth().weight(0.8f / 3f)) { // 3 cards + see all with 20% width
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        onClick = { navAction(DiscoverScreenNavActions.GoShowDetails(movieOrShow.tmdbId)) },
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

            Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                SeeAllCard { seeAllAction() }
            }
        }
    }
}
