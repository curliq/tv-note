package com.free.tvtracker.ui.person.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.NonLazyGrid
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.dialogs.DetailsSheetHeader
import com.free.tvtracker.ui.person.PersonScreenNavAction
import com.free.tvtracker.ui.person.PersonUiModel
import com.free.tvtracker.ui.person.PersonUiState
import com.free.tvtracker.ui.person.PersonViewModel

@Composable
fun PersonShowsSheet(
    viewModel: PersonViewModel,
    navActions: (PersonScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.result.collectAsState().value as PersonUiState.Ok
    TvTrackerTheme {
        Scaffold {
            PersonShowsContent(show.data, navActions, true, bottomPadding)
        }
    }
}

@Composable
fun PersonMoviesSheet(
    viewModel: PersonViewModel,
    navActions: (PersonScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.result.collectAsState().value as PersonUiState.Ok
    TvTrackerTheme {
        PersonShowsContent(show.data, navActions, false, bottomPadding)
    }
}

@Composable
fun PersonShowsContent(
    person: PersonUiModel,
    action: (PersonScreenNavAction) -> Unit,
    isTvShows: Boolean,
    bottomPadding: Float = 0f
) {
    LazyColumn {
        stickyHeader {
            DetailsSheetHeader("Acting")
        }
        item {
            Content(if (isTvShows) person.tvShowsCast else person.moviesCast) { content ->
                action(PersonScreenNavAction.GoShowDetails(content.tmdbId, isTvShows))
            }
        }
        stickyHeader {
            DetailsSheetHeader("Part of the crew")
        }
        item {
            Content(if (isTvShows) person.tvShowsCrew else person.moviesCrew) { content ->
                action(PersonScreenNavAction.GoShowDetails(content.tmdbId, isTvShows))
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp))
        }
    }
}

@Composable
private fun Content(shows: List<PersonUiModel.Credit>, onClick: (PersonUiModel.Credit) -> Unit) {
    if (shows.isEmpty()) {
        Box(
            Modifier.fillMaxWidth().height(72.dp).padding(horizontal = TvTrackerTheme.sidePadding),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Nothing yet",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
    NonLazyGrid(
        columns = 3,
        itemCount = shows.size,
        Modifier.padding(TvTrackerTheme.sidePadding)
    ) { index ->
        val show = shows[index]
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            onClick = { onClick(show) },
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(Modifier.aspectRatio(posterRatio())) {
                TvImage(show.posterUrl, modifier = Modifier.fillMaxSize())
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.padding(8.dp)) {
                Text(
                    show.name,
                    minLines = 1,
                    maxLines = 3,
                    overflow = Ellipsis,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
