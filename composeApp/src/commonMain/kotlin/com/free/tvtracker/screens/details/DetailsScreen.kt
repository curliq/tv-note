package com.free.tvtracker.screens.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.justwatch_logo_lightmode
import besttvtracker.composeapp.generated.resources.tmdb_logo
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingIndicator
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.composables.NonLazyGrid
import com.free.tvtracker.core.composables.ResImage
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.ScreenContentAnimation
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.screens.details.DetailsUiModel.TrackingStatus.Action.*
import org.jetbrains.compose.resources.ExperimentalResourceApi

sealed class DetailsScreenNavAction {
    data class GoYoutube(val webUrl: String) : DetailsScreenNavAction()
    data object GoAllEpisodes : DetailsScreenNavAction()
    data object GoMedia : DetailsScreenNavAction()
    data object GoCastAndCrew : DetailsScreenNavAction()
    data class GoCastAndCrewDetails(val personTmdbId: Int) : DetailsScreenNavAction()
}

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    showId: Int,
    navAction: (DetailsScreenNavAction) -> Unit,
    modifier: Modifier = Modifier
) {
    TvTrackerTheme {
        Scaffold(modifier.fillMaxSize()) {
            LaunchedEffect(showId) {
                viewModel.setId(showId)
            }
            val show = viewModel.result.collectAsState().value
            AnimatedContent(
                show,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    DetailsUiState.Error -> ErrorScreen { viewModel.setId(showId) }
                    DetailsUiState.Loading -> LoadingScreen()
                    is DetailsUiState.Ok -> DetailsScreenContent(targetState.data, navAction, viewModel::action)
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DetailsScreenContent(
    show: DetailsUiModel,
    navAction: (DetailsScreenNavAction) -> Unit,
    showAction: (DetailsViewModel.DetailsAction) -> Unit
) {
    Column(Modifier.padding(horizontal = sidePadding).fillMaxWidth().verticalScroll(rememberScrollState())) {
        Row(Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            OutlinedCard {
                Box(
                    Modifier.width(100.dp).aspectRatio(posterRatio())
                ) {
                    TvImage(show.posterUrl, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = show.name, style = TvTrackerTheme.Typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = show.releaseStatus, style = TvTrackerTheme.Typography.bodySmall)
            }
        }
        Row {
            fun text(action: DetailsUiModel.TrackingStatus.Action?): String = when (action) {
                RemoveFromWatchlist -> "De-watchlist"
                RemoveFromWatching -> "De-list"
                TrackWatchlist -> "Add to watchlist"
                TrackWatching -> "Add to watching"
                MoveToWatchlist -> "Move to watchlist"
                MoveToWatching -> "Move to watching"
                null -> ""
            }
            show.trackingStatus.action1?.let { action ->
                Button(
                    onClick = { showAction(DetailsViewModel.DetailsAction.TrackingAction(show.tmdbId, action)) },
                    modifier = Modifier.weight(0.5f, true)
                ) {
                    if (!show.trackingStatus.isLoading) {
                        Text(text(action))
                    } else {
                        LoadingIndicator(
                            modifier = Modifier.height(24.dp).aspectRatio(1f),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            show.trackingStatus.action2?.let { action ->
                OutlinedButton(
                    onClick = { showAction(DetailsViewModel.DetailsAction.TrackingAction(show.tmdbId, action)) },
                    modifier = Modifier.weight(0.5f, true)
                ) {
                    val color = when (action) {
                        RemoveFromWatchlist, RemoveFromWatching -> Color.Red
                        else -> Color.Unspecified
                    }
                    if (!show.trackingStatus.isLoading) {
                        Text(text(show.trackingStatus.action2), color = color)
                    } else {
                        LoadingIndicator(
                            modifier = Modifier.height(24.dp).aspectRatio(1f),
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text("Where to watch", style = TvTrackerTheme.Typography.titleLarge)
        Row(verticalAlignment = Alignment.Bottom) {
            Text("Source:  ", style = TvTrackerTheme.Typography.labelSmall)
            val tint = if (isSystemInDarkTheme()) {
                //todo test on ios
                Color(0xffFBD446)
            } else {
                null
            }
            ResImage(
                Res.drawable.justwatch_logo_lightmode,
                contentDescription = "justwatch",
                modifier = Modifier.height(20.dp),
                tint = tint
            )
        }
        Spacer(Modifier.height(8.dp))
        NonLazyGrid(5, show.watchProviders.size) { index ->
            val show = show.watchProviders[index]
            Box(Modifier.aspectRatio(1f)) {
                TvImage(show.logo, modifier = Modifier.fillMaxSize())
            }
        }
        Spacer(Modifier.height(24.dp))

        Text("Plot", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = show.description ?: "No description available")
        Spacer(Modifier.height(24.dp))

        if (!show.genres.isNullOrEmpty()) {
            Text("Genres", style = TvTrackerTheme.Typography.titleLarge)
            Text(text = show.genres)
            Spacer(Modifier.height(24.dp))
        }

        Text(text = "Episodes", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = show.seasonsInfo ?: "No seasons available")
        Spacer(Modifier.height(8.dp))
        FilledTonalButton(
            onClick = { navAction(DetailsScreenNavAction.GoAllEpisodes) },
            content = { Text("See all episodes") },
            shape = TvTrackerTheme.ShapeButton
        )
        Spacer(Modifier.height(24.dp))

        Text("Trailers & photos", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            show.mediaTrailer?.let { trailer ->
                Box(Modifier.fillMaxWidth().weight(0.4f)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        onClick = { navAction(DetailsScreenNavAction.GoYoutube(trailer.videoUrl)) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        MediaVideoCard(trailer = trailer)
                    }
                }
            }
            show.mediaMostPopularImage?.let { url ->
                Box(Modifier.fillMaxWidth().weight(0.4f)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(Modifier.aspectRatio(1f)) {
                            TvImage(url, modifier = Modifier.fillMaxHeight().fillMaxWidth())
                        }
                    }
                }
            }
            Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                SeeAllCard { navAction(DetailsScreenNavAction.GoMedia) }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text(text = "Cast & crew", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            show.castFirst?.let {
                Box(Modifier.fillMaxWidth().weight(0.4f)) {
                    CastCard(it) { navAction(DetailsScreenNavAction.GoCastAndCrewDetails(it.tmdbId)) }
                }
            }
            show.castSecond?.let {
                Box(Modifier.fillMaxWidth().weight(0.4f)) {
                    CastCard(it) { navAction(DetailsScreenNavAction.GoCastAndCrewDetails(it.tmdbId)) }
                }
            }
            Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                SeeAllCard { navAction(DetailsScreenNavAction.GoCastAndCrew) }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text(text = "Reviews & ratings", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.fillMaxWidth(0.3f)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium)
                        )
                        .clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    ResImage(Res.drawable.tmdb_logo, "tmdb", modifier = Modifier.fillMaxWidth().aspectRatio(1f))
                }
                Column(modifier = Modifier.padding(8.dp)) {
                    Row {
                        Icon(Icons.Rounded.Star, "rating", Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(show.ratingTmdbVoteAverage)
                    }
                    Row {
                        Icon(Icons.Rounded.Person, "person", Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(show.ratingTmdbVoteCount)
                    }
                }
            }
            Text(
                "I will add IMDB and rotten tomatoes at some point.",
                style = TvTrackerTheme.Typography.labelMedium,
                modifier = Modifier.padding(horizontal = 24.dp).align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun MediaVideoCard(trailer: DetailsUiModel.Video) {
    Box(Modifier.aspectRatio(1f)) {
        TvImage(trailer.thumbnail, modifier = Modifier.fillMaxSize())
        Box(Modifier.align(Alignment.Center)) {
            Box(
                Modifier.size(48.dp).clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            )
            Icon(
                Icons.Rounded.PlayArrow,
                "play",
                modifier = Modifier.align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun BoxScope.SeeAllCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize().align(Alignment.Center),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            Modifier.fillMaxSize().clickable { onClick() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "See\nAll",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                "open",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
internal fun CastCard(person: DetailsUiModel.Person, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        onClick = { onClick() },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.aspectRatio(1f)) {
            TvImage(person.photo, modifier = Modifier.fillMaxSize())
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.padding(8.dp)) {
            Text(
                person.irlName,
                minLines = 1,
                maxLines = 2,
                overflow = Ellipsis,
                style = TvTrackerTheme.Typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                when (person) {
                    is DetailsUiModel.Cast -> person.characterName; is DetailsUiModel.Crew -> person.job
                },
                minLines = 1,
                maxLines = 2,
                overflow = Ellipsis,
                style = TvTrackerTheme.Typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}
