package com.free.tvtracker.ui.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_open_window
import besttvtracker.composeapp.generated.resources.imdb_logo
import besttvtracker.composeapp.generated.resources.justwatch_logo_lightmode
import besttvtracker.composeapp.generated.resources.rotten_tomatoes_logo
import besttvtracker.composeapp.generated.resources.tmdb_logo
import com.free.tvtracker.core.Logger
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.composables.NonLazyGrid
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.ScreenContentAnimation
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.MoveMovieToFinished
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.MoveToWatching
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.MoveToWatchlist
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.RemoveFromWatching
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.RemoveFromWatchlist
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.RemoveMovieFromWatched
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.TrackWatching
import com.free.tvtracker.ui.details.DetailsUiModel.TrackingStatus.Action.TrackWatchlist
import org.jetbrains.compose.resources.DrawableResource

sealed class DetailsScreenNavAction {
    data class GoYoutube(val webUrl: String) : DetailsScreenNavAction()
    data object GoAllEpisodes : DetailsScreenNavAction()
    data object GoReviews : DetailsScreenNavAction()
    data object GoMedia : DetailsScreenNavAction()
    data object GoCastAndCrew : DetailsScreenNavAction()
    data object GoFilmCollection : DetailsScreenNavAction()
    data object GoManageWatchlists : DetailsScreenNavAction()
    data object HideManageWatchlists : DetailsScreenNavAction()
    data class GoCastAndCrewDetails(val personTmdbId: Int) : DetailsScreenNavAction()
    data class GoContentDetails(val tmdbId: Int, val isTvShow: Boolean) : DetailsScreenNavAction()
    data class GoWebsite(val url: String) : DetailsScreenNavAction()
}

@Composable
fun DetailsScreen(
    viewModel: ContentDetailsViewModel,
    content: ContentDetailsViewModel.LoadContent,
    navAction: (DetailsScreenNavAction) -> Unit,
    modifier: Modifier = Modifier
) {
    TvTrackerTheme {
        Scaffold(modifier.fillMaxSize()) {
            val show = viewModel.result.collectAsState().value
            Logger().d("load details screen, ${content}, show: ${show.hashCode()}", "DetailsScreen")
            LaunchedEffect(content) {
                viewModel.loadContent(content)
            }
            val isActionsAllowed = viewModel.isActionsAllowed.collectAsState(true).value
            AnimatedContent(
                show,
                transitionSpec = ScreenContentAnimation(),
                contentKey = { targetState -> targetState::class }
            ) { targetState ->
                when (targetState) {
                    DetailsUiState.Error -> ErrorScreen { viewModel.loadContent(content) }
                    DetailsUiState.Loading -> LoadingScreen()
                    is DetailsUiState.Ok -> DetailsScreenContent(
                        targetState.data,
                        isActionsAllowed,
                        navAction,
                        viewModel::action
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsScreenContent(
    show: DetailsUiModel,
    isActionsAllowed: Boolean,
    navAction: (DetailsScreenNavAction) -> Unit,
    showAction: (ContentDetailsViewModel.DetailsAction) -> Unit
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
                Text(text = show.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = show.releaseStatus, style = MaterialTheme.typography.bodySmall)
                if (show.duration != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Duration: ${show.duration}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Row {
            fun text(action: DetailsUiModel.TrackingStatus.Action?): String = when (action) {
                RemoveFromWatchlist -> "Remove"
                RemoveFromWatching -> "Remove"
                RemoveMovieFromWatched -> "Remove"
                TrackWatchlist -> "Add to watchlist"
                TrackWatching -> "Watching now"
                MoveToWatchlist -> "Move to watchlist"
                MoveToWatching -> "Move to watching"
                MoveMovieToFinished -> "Mark watched"
                DetailsUiModel.TrackingStatus.Action.ManageWatchlists -> "Add to list"
                null -> ""
            }
            show.trackingStatus.action1?.let { action1 ->
                AnimatedContent(
                    modifier = Modifier.weight(0.5f, true),
                    targetState = action1,
                    transitionSpec = {
                        val enterTransition = slideInVertically(
                            animationSpec = tween(durationMillis = 330),
                            initialOffsetY = { fullHeight -> -fullHeight }
                        ) + fadeIn(animationSpec = tween(durationMillis = 330))
                        val exitTransition = slideOutVertically(
                            animationSpec = tween(durationMillis = 330),
                            targetOffsetY = { fullHeight -> fullHeight }
                        ) + fadeOut(animationSpec = tween(durationMillis = 330))
                        enterTransition togetherWith exitTransition
                    }
                ) { state ->
                    Box(modifier = Modifier.fillMaxWidth().padding()) {
                        Button(
                            onClick = {
                                showAction(
                                    ContentDetailsViewModel.DetailsAction.TrackingAction(
                                        show,
                                        state,
                                        navAction
                                    )
                                )
                            },
                            shape = TvTrackerTheme.ShapeButton,
                            enabled = isActionsAllowed,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text(state))
                        }
                        if (show.trackingStatus.isLoading) {
                            Row(modifier = Modifier.padding(end = 8.dp).align(Alignment.CenterEnd)) {
                                LoadingIndicator(
                                    modifier = Modifier.height(16.dp).aspectRatio(1f),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
                if (show.trackingStatus.action2 != null) {
                    Spacer(Modifier.width(8.dp))
                }
            }
            show.trackingStatus.action2?.let { action2 ->
                AnimatedContent(
                    modifier = Modifier.weight(0.5f, true),
                    targetState = action2,
                    transitionSpec = {
                        val enterTransition = slideInVertically(
                            animationSpec = tween(durationMillis = 330),
                            initialOffsetY = { fullHeight -> -fullHeight }
                        ) + fadeIn(animationSpec = tween(durationMillis = 330))
                        val exitTransition = slideOutVertically(
                            animationSpec = tween(durationMillis = 330),
                            targetOffsetY = { fullHeight -> fullHeight }
                        ) + fadeOut(animationSpec = tween(durationMillis = 330))
                        enterTransition togetherWith exitTransition
                    }
                ) { state ->
                    val color = when (state) {
                        RemoveFromWatchlist, RemoveFromWatching, RemoveMovieFromWatched -> {
                            MaterialTheme.colorScheme.error
                        }

                        else -> {
                            MaterialTheme.colorScheme.primary
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding()) {
                        OutlinedButton(
                            onClick = {
                                showAction(
                                    ContentDetailsViewModel.DetailsAction.TrackingAction(
                                        show,
                                        state,
                                        navAction
                                    )
                                )
                            },
                            shape = TvTrackerTheme.ShapeButton,
                            enabled = isActionsAllowed,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text(state))
                        }
                        if (show.trackingStatus.isLoading) {
                            Row(modifier = Modifier.padding(end = 8.dp).align(Alignment.CenterEnd)) {
                                LoadingIndicator(
                                    modifier = Modifier.height(16.dp).aspectRatio(1f),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text("Where to watch", style = MaterialTheme.typography.titleLarge)
        Row(verticalAlignment = Alignment.Bottom) {
            Text("Country: ${show.watchProviderCountry}, ", style = MaterialTheme.typography.labelSmall)
            Text("Source:  ", style = MaterialTheme.typography.labelSmall)
            ResImage(
                Res.drawable.justwatch_logo_lightmode,
                contentDescription = "justwatch",
                modifier = Modifier.height(20.dp),
                tint = Color(0xffDDBC43)
            )
        }
        Spacer(Modifier.height(8.dp))
        if (show.watchProviders.isEmpty()) {
            Text(
                text = "No channel available",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        } else {
            NonLazyGrid(5, show.watchProviders.size) { index ->
                val show = show.watchProviders[index]
                Box(Modifier.aspectRatio(1f)) {
                    TvImage(show.logo, modifier = Modifier.fillMaxSize())
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        if (show.isTvShow) {
            Text(text = "Episodes", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(text = show.seasonsInfo ?: "No seasons available")
            Spacer(Modifier.height(8.dp))
            Row {
                FilledTonalButton(
                    onClick = { navAction(DetailsScreenNavAction.GoAllEpisodes) },
                    content = { Text("See all episodes") },
                    shape = TvTrackerTheme.ShapeButton
                )
            }
            Spacer(Modifier.height(24.dp))
        }

        Text("Plot", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = show.description ?: "No description available")
        Spacer(Modifier.height(24.dp))
        if (!show.genres.isNullOrEmpty()) {
            Text("Genres", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                show.genres.forEach { text ->
                    AssistChip(onClick = {}, label = { Text(text = text) })
                }
            }
            Spacer(Modifier.height(24.dp))
        }
        if (show.movieSeries?.movies?.isNotEmpty() == true) {
            Text(
                text = "Film series",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            show.movieSeries.overview?.let {
                Text(text = it)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                show.movieSeries.movies.getOrNull(0)?.let {
                    Box(Modifier.fillMaxWidth().weight(0.4f)) {
                        FilmSeriesCard(it, navAction)
                    }
                }
                show.movieSeries.movies.getOrNull(1)?.let {
                    Box(Modifier.fillMaxWidth().weight(0.4f)) {
                        FilmSeriesCard(it, navAction)
                    }
                    Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                        SeeAllCard("\n(${show.movieSeries.movies.size})") {
                            navAction(DetailsScreenNavAction.GoFilmCollection)
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
        Text("Trailers & photos", style = MaterialTheme.typography.titleLarge)
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

        Text(text = "Cast & crew", style = MaterialTheme.typography.titleLarge)
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

        Text(text = "Reviews & ratings", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(Modifier.weight(0.333f).fillMaxHeight()) {
                RatingsCard(show.ratingTmdbVoteAverage, show.ratingTmdbVoteCount, Res.drawable.tmdb_logo)
            }
            Box(Modifier.weight(0.333f).fillMaxHeight()) {
                if (show.omdbRatings?.imdbVoteCount != null && show.omdbRatings.imdbRating != null) {
                    RatingsCard(show.omdbRatings.imdbRating, show.omdbRatings.imdbVoteCount, Res.drawable.imdb_logo)
                }
            }
            Box(Modifier.weight(0.333f, fill = false).fillMaxHeight()) {
                if (show.omdbRatings?.tomatoesRatingPercentage != null) {
                    RatingsCard(show.omdbRatings.tomatoesRatingPercentage, null, Res.drawable.rotten_tomatoes_logo)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        FilledTonalButton(
            onClick = {
                showAction(ContentDetailsViewModel.DetailsAction.LoadReviews(show.imdbId))
                navAction(DetailsScreenNavAction.GoReviews)
            },
            content = { Text("See reviews") },
            shape = TvTrackerTheme.ShapeButton
        )
        Spacer(Modifier.height(24.dp))

        Text(text = "About", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Website", style = MaterialTheme.typography.titleSmall)
        if (!show.website.isNullOrEmpty()) {
            FilledTonalButton(
                onClick = { navAction(DetailsScreenNavAction.GoWebsite(show.website)) },
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 12.dp),
                shape = TvTrackerTheme.ShapeButton,
            ) {
                ResImage(
                    res = Res.drawable.ic_open_window,
                    contentDescription = "open",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Text(text = show.website)
            }
        } else {
            Text("(Website not found)", style = MaterialTheme.typography.labelMedium)
        }
        Spacer(Modifier.height(8.dp))
        Text("IMDB page", style = MaterialTheme.typography.titleSmall)
        if (show.imdbUrl != null) {
            FilledTonalButton(
                onClick = { navAction(DetailsScreenNavAction.GoWebsite(show.imdbUrl)) },
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 12.dp),
                shape = TvTrackerTheme.ShapeButton,
            ) {
                ResImage(
                    res = Res.drawable.ic_open_window,
                    contentDescription = "open",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Text(text = show.imdbUrl)
            }
        } else {
            Text("(IMDB page not found)", style = MaterialTheme.typography.labelMedium)
        }
        if (!show.isTvShow) {
            Spacer(Modifier.height(8.dp))
            Text("Budget", style = MaterialTheme.typography.titleSmall)
            Text(show.budget, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Text("Revenue", style = MaterialTheme.typography.titleSmall)
            Text(show.revenue, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FilmSeriesCard(movie: DetailsUiModel.MovieSeries.Movie, navAction: (DetailsScreenNavAction) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        onClick = { navAction(DetailsScreenNavAction.GoContentDetails(movie.tmdbId, false)) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.aspectRatio(posterRatio())) {
            TvImage(movie.posterUrl, modifier = Modifier.fillMaxSize())
        }
        Spacer(Modifier.height(8.dp))
        Text(
            movie.name,
            minLines = 1,
            maxLines = 2,
            overflow = Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            movie.year,
            overflow = Ellipsis,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(8.dp))
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
fun BoxScope.SeeAllCard(textAppend: String = "", onClick: () -> Unit) {
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
                "More$textAppend",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
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
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                when (person) {
                    is DetailsUiModel.Cast -> person.characterName; is DetailsUiModel.Crew -> person.job
                },
                minLines = 1,
                maxLines = 2,
                overflow = Ellipsis,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Composable
private fun RatingsCard(rating: String, voteCount: String?, icon: DrawableResource) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium)
                )
                .clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))
        ) {
            ResImage(icon, "tmdb", modifier = Modifier.fillMaxWidth().aspectRatio(1f))
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Star, "rating", Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text(rating, style = MaterialTheme.typography.bodySmall)
            }
            if (voteCount == null) return@Card
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Person, "person", Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text(voteCount, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
