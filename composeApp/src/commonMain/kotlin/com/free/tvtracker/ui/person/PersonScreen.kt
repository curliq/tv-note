package com.free.tvtracker.ui.person

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.instagram
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.details.SeeAllCard

sealed class PersonScreenNavAction {
    data class GoShowDetails(val tmdbShowId: Int, val isTvShow: Boolean) : PersonScreenNavAction()
    data object GoAllShows : PersonScreenNavAction()
    data object GoAllMovies : PersonScreenNavAction()
    data object GoAllPhotos : PersonScreenNavAction()
    data class GoInstagram(val url: String) : PersonScreenNavAction()
}

@Composable
fun PersonScreen(
    viewModel: PersonViewModel,
    personId: Int,
    navAction: (PersonScreenNavAction) -> Unit,
    modifier: Modifier = Modifier
) {
    TvTrackerTheme {
        Scaffold(modifier.fillMaxSize()) {
            val person = viewModel.result.collectAsState().value
            LaunchedEffect(personId) {
                if (person !is PersonUiState.Ok) { // prevents always loading on ios
                    viewModel.setPersonId(personId)
                }
            }
            when (person) {
                PersonUiState.Error -> ErrorScreen(refresh = { viewModel.setPersonId(personId) })
                PersonUiState.Loading -> LoadingScreen()
                is PersonUiState.Ok -> PersonContent(person.data, navAction)
            }
        }
    }
}

@Composable
fun PersonContent(person: PersonUiModel, navAction: (PersonScreenNavAction) -> Unit) {
    Column(Modifier.padding(horizontal = sidePadding).fillMaxWidth().verticalScroll(rememberScrollState())) {
        Row(Modifier.fillMaxWidth().padding(top = 16.dp)) {
            OutlinedCard {
                Box(
                    Modifier.width(100.dp)
                ) {
                    TvImage(person.photoUrl, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = person.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = person.job, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                if (person.instagramTag != null) {
                    FilledTonalButton(
                        onClick = { navAction(PersonScreenNavAction.GoInstagram(person.instagramUrl!!)) },
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 12.dp),
                        shape = TvTrackerTheme.ShapeButton,
                    ) {
                        ResImage(
                            res = Res.drawable.instagram,
                            contentDescription = "instagram",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(text = person.instagramTag)
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text("About", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Birthday: ${person.dob}")
        Text("Birthplace: ${person.bornIn}")
        Spacer(Modifier.height(8.dp))
        Text(person.bio)
        Spacer(Modifier.height(24.dp))

        Text("Movies (${person.moviesCount})", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        MoviesOrShowsRow(person.firstTwoMovies, navAction)
        Spacer(Modifier.height(24.dp))

        Text("Tv shows (${person.tvShowsCount})", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        MoviesOrShowsRow(person.firstTwoTvShows, navAction)
        Spacer(Modifier.height(24.dp))

        Text("Photos", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            person.firstTwoPhotos.forEach { photo ->
                Box(Modifier.fillMaxWidth().weight(0.4f)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(Modifier.aspectRatio(1f)) {
                            TvImage(photo, modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
            if (person.photos.size > 2) {
                Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                    SeeAllCard { navAction(PersonScreenNavAction.GoAllPhotos) }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun MoviesOrShowsRow(moviesOrShows: List<PersonUiModel.Credit>, navAction: (PersonScreenNavAction) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        moviesOrShows.forEach { movieOrShow ->
            Box(Modifier.fillMaxWidth().weight(0.4f)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    onClick = {
                        navAction(
                            PersonScreenNavAction.GoShowDetails(
                                movieOrShow.tmdbId,
                                movieOrShow.isTvShow
                            )
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(Modifier.aspectRatio(posterRatio())) {
                        TvImage(movieOrShow.posterUrl, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.padding(8.dp)) {
                        Text(
                            movieOrShow.name,
                            minLines = 1,
                            maxLines = 3,
                            overflow = Ellipsis,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
        if (moviesOrShows.size == 2) {
            Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                SeeAllCard {
                    if (moviesOrShows.firstOrNull()?.isTvShow == true) {
                        navAction(PersonScreenNavAction.GoAllShows)
                    } else {
                        navAction(PersonScreenNavAction.GoAllMovies)
                    }
                }
            }
        }
    }

}
