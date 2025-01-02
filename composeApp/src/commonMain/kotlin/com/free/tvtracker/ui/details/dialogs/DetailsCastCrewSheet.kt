package com.free.tvtracker.ui.details.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.NonLazyGrid
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.CastCard
import com.free.tvtracker.ui.details.DetailsScreenNavAction
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.ui.details.DetailsUiState
import com.free.tvtracker.ui.details.DetailsViewModel

@Composable
fun DetailsCastCrewSheet(
    viewModel: DetailsViewModel,
    navActions: (DetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.result.collectAsState().value as DetailsUiState.Ok
    TvTrackerTheme {
        Scaffold {
            DetailsCastCrewContent(show.data, navActions, bottomPadding)
        }
    }
}

@Composable
fun DetailsCastCrewContent(
    show: DetailsUiModel,
    navActions: (DetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        stickyHeader {
            DetailsSheetHeader("Cast")
        }
        item {
            PersonGrid(show.cast) { item ->
                val cast = item as DetailsUiModel.Cast
                CastCard(cast) { navActions(DetailsScreenNavAction.GoCastAndCrewDetails(item.tmdbId)) }
            }
        }
        stickyHeader {
            DetailsSheetHeader("Crew")
        }
        item {
            PersonGrid(show.crew) { item ->
                val crew = item as DetailsUiModel.Crew
                CastCard(crew) { navActions(DetailsScreenNavAction.GoCastAndCrewDetails(item.tmdbId)) }
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp))
        }
    }
}

@Composable
private fun PersonGrid(items: List<Any>, ui: @Composable (Any) -> Unit) {
    if (items.isEmpty()) {
        Box(
            Modifier.fillMaxWidth().height(72.dp).padding(horizontal = TvTrackerTheme.sidePadding),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "No people found",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    } else {
        NonLazyGrid(
            columns = 3,
            itemCount = items.size,
            Modifier.padding(TvTrackerTheme.sidePadding)
        ) { index ->
            val video = items[index]
            ui(video)
        }
    }
}
