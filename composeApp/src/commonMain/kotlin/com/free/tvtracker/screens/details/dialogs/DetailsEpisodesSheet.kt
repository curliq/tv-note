package com.free.tvtracker.screens.details.dialogs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.screens.details.DetailsUiModel
import com.free.tvtracker.screens.details.DetailsUiState
import com.free.tvtracker.screens.details.DetailsViewModel

@Composable
fun DetailsEpisodesSheet(viewModel: DetailsViewModel, bottomPadding: Float = 0f) {
    val show = viewModel.result.collectAsState().value as DetailsUiState.Ok
    TvTrackerTheme {
        DetailsEpisodesContent(show.data, viewModel::action, bottomPadding)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsEpisodesContent(
    show: DetailsUiModel,
    action: (DetailsViewModel.DetailsAction) -> Unit,
    bottomPadding: Float = 0f
) {
    LazyColumn {
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
                                    shape = RoundedCornerShape(8.dp),
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
                                        style = TvTrackerTheme.Typography.labelMedium
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
