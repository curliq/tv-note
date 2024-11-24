package com.free.tvtracker.ui.details.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.NonLazyGrid
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.details.DetailsScreenNavAction
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.ui.details.DetailsUiState
import com.free.tvtracker.ui.details.DetailsViewModel

@Composable
fun DetailsFilmCollectionSheet(
    viewModel: DetailsViewModel,
    navAction: (DetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val data = viewModel.result.collectAsState().value as DetailsUiState.Ok
    TvTrackerTheme {
        Scaffold {
            DetailsFilmCollectionContent(data.data.movieSeries!!, navAction, bottomPadding)
        }
    }
}

@Composable
fun DetailsFilmCollectionContent(
    collection: DetailsUiModel.MovieSeries,
    action: (DetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    LazyColumn {
        item {
            NonLazyGrid(
                columns = 3,
                itemCount = collection.movies.size,
                Modifier.padding(TvTrackerTheme.sidePadding)
            ) { index ->
                val movie = collection.movies[index]
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    onClick = { action(DetailsScreenNavAction.GoContentDetails(movie.tmdbId, false)) },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(Modifier.aspectRatio(posterRatio())) {
                        TvImage(movie.posterUrl, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        movie.name,
                        minLines = 1,
                        maxLines = 3,
                        overflow = Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        movie.year,
                        minLines = 1,
                        maxLines = 3,
                        overflow = Ellipsis,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp))
        }
    }
}
