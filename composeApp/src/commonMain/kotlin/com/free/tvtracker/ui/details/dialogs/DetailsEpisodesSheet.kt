package com.free.tvtracker.ui.details.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.ui.details.DetailsUiState
import com.free.tvtracker.ui.details.DetailsViewModel

@Composable
fun DetailsEpisodesSheet(viewModel: DetailsViewModel, bottomPadding: Float = 0f) {
    val show = viewModel.result.collectAsState().value as? DetailsUiState.Ok
    if (show == null) return
    TvTrackerTheme {
        Scaffold {
            DetailsEpisodesContent(show.data, viewModel::action, bottomPadding)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsEpisodesContent(
    show: DetailsUiModel,
    action: (DetailsViewModel.DetailsAction) -> Unit,
    bottomPadding: Float = 0f
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        item {
            if (show.seasons?.any { it.isWatchable } == true) {
                Button(
                    onClick = {
                        action(
                            DetailsViewModel.DetailsAction.MarkShowWatched(
                                show.tmdbId,
                                show.trackedContentId
                            )
                        )
                    },
                    shape = TvTrackerTheme.ShapeButton,
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = sidePadding, top = 0.dp, end = sidePadding, bottom = sidePadding),
                ) {
                    Text("Mark all as watched")
                }
            } else if (false) { //todo
                OutlinedButton(
                    onClick = {
                        action(
                            DetailsViewModel.DetailsAction.MarkShowWatched(
                                show.tmdbId,
                                show.trackedContentId
                            )
                        )
                    },
                    shape = TvTrackerTheme.ShapeButton,
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = sidePadding, top = 0.dp, end = sidePadding, bottom = sidePadding),
                ) {
                    Text("Reset all episodes")
                }
            }
        }
        show.seasons?.forEach { season ->
            stickyHeader {
                Column(Modifier.fillParentMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerLow)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            season.seasonTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding, vertical = 8.dp)
                                .weight(1f)
                        )
                        if (season.isWatchable) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                                FilledTonalButton(
                                    modifier = Modifier.height(28.dp),
                                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                                    shape = TvTrackerTheme.ShapeButton,
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                    ),
                                    onClick = {
                                        action(
                                            DetailsViewModel.DetailsAction.MarkSeasonWatched(
                                                season.seasonId,
                                                show.tmdbId
                                            )
                                        )
                                    },
                                ) {
                                    Text(
                                        text = "Mark season as watched",
                                        style = TextStyle(
                                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                                            fontWeight = MaterialTheme.typography.labelMedium.fontWeight
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else if (season.watched) {
                            Text(
                                "Season watched", style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            )
                        }
                        Spacer(Modifier.width(TvTrackerTheme.sidePadding))
                    }
                    HorizontalDivider()
                }
            }
            itemsIndexed(season.episodes) { index, ep ->
                if (index == 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Box(Modifier.padding(horizontal = TvTrackerTheme.sidePadding, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TvImage(
                            ep.thumbnail,
                            modifier = Modifier.size(48.dp),
                            containerModifier = Modifier.align(Alignment.Top)
                        )
                        Spacer(modifier = Modifier.width(TvTrackerTheme.sidePadding))
                        Row(modifier = Modifier.align(Alignment.Top), verticalAlignment = Alignment.CenterVertically) {
                            val textMeasurer = rememberTextMeasurer()
                            val textLayoutResult: TextLayoutResult =
                                textMeasurer.measure(
                                    text = AnnotatedString(
                                        if (season.episodes.size <= 9) "0"
                                        else if (season.episodes.size <= 99) "00"
                                        else "000"
                                    ),
                                    style = LocalTextStyle.current
                                )
                            val textSize = textLayoutResult.size
                            val density = LocalDensity.current
                            Text(ep.number, modifier = Modifier.width(with(density) { textSize.width.toDp() }))
                            Box(Modifier.padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                                Box(
                                    Modifier.size(4.dp).clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.outlineVariant),
                                )
                            }
                        }
                        Column(Modifier.weight(1f).align(Alignment.Top)) {
                            Text(ep.name)
                            Text(
                                ep.releaseDate,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(TvTrackerTheme.sidePadding))
                        if (ep.watched) {
                            Text(
                                "Watched",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.align(Alignment.Top)
                            )
                        }
                    }
                }
                if (index == season.episodes.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp))
        }
    }
}
