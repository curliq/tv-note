package com.free.tvtracker.screens.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.free.tvtracker.core.composables.ErrorScreen
import com.free.tvtracker.core.composables.LoadingScreen
import com.free.tvtracker.core.composables.NonLazyGrid
import com.free.tvtracker.core.composables.ResImage
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.ScreenContentAnimation
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.theme.TvTrackerTheme.sidePadding
import org.jetbrains.compose.resources.ExperimentalResourceApi

sealed class DetailsScreenNavAction {
    data class GoYoutube(val webUrl: String) : DetailsScreenNavAction()
    data object GoAllEpisodes : DetailsScreenNavAction()
    data object GoMedia : DetailsScreenNavAction()
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
            LaunchedEffect(viewModel) {
                viewModel.setId(showId)
            }
            val show = viewModel.result.collectAsState().value
            AnimatedContent(show, transitionSpec = ScreenContentAnimation()) { targetState ->
                when (targetState) {
                    DetailsUiState.Error -> ErrorScreen { viewModel.setId(showId) }
                    DetailsUiState.Loading -> LoadingScreen()
                    is DetailsUiState.Ok -> DetailsScreenContent(targetState.data, navAction)
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DetailsScreenContent(show: DetailsUiModel, navAction: (DetailsScreenNavAction) -> Unit) {
    Column(Modifier.padding(horizontal = sidePadding).fillMaxWidth().verticalScroll(rememberScrollState())) {
        Row(Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            OutlinedCard {
                Box(
                    Modifier.width(80.dp).aspectRatio(posterRatio())
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
        Text(text = show.trackingStatus)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Add to watchlist")
        }
        Spacer(Modifier.height(24.dp))
        Text("Where to watch", style = TvTrackerTheme.Typography.titleLarge)
        Row(verticalAlignment = Alignment.Bottom) {
            Text("Source:  ", style = TvTrackerTheme.Typography.labelSmall)
            //todo test on ios
            val tint = if (isSystemInDarkTheme()) {
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
                        modifier = Modifier.fillMaxSize()
                            .clickable { navAction(DetailsScreenNavAction.GoYoutube(trailer.videoUrl)) }
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
        Text(text = "Episodes", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = show.seasonsInfo ?: "No seasons available")
        Spacer(Modifier.height(8.dp))
        FilledTonalButton(
            onClick = { navAction(DetailsScreenNavAction.GoAllEpisodes) },
            content = { Text("See all episodes") })
        Spacer(Modifier.height(24.dp))
        Text(text = "Cast & crew", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            show.castFirst?.let { Box(Modifier.fillMaxWidth().weight(0.4f)) { CastCard(it) } }
            show.castSecond?.let { Box(Modifier.fillMaxWidth().weight(0.4f)) { CastCard(it) } }
            Box(Modifier.fillMaxWidth().weight(0.2f).fillMaxHeight()) {
                SeeAllCard({ })
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(text = "Reviews", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Row {
            //todo
        }
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
private fun BoxScope.SeeAllCard(onClick: () -> Unit) {
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
private fun CastCard(cast: DetailsUiModel.Cast) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.aspectRatio(1f)) {
            TvImage(cast.photo, modifier = Modifier.fillMaxHeight().fillMaxWidth())
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.padding(8.dp)) {
            Text(
                cast.irlName,
                minLines = 1,
                maxLines = 2,
                overflow = Ellipsis,
                style = TvTrackerTheme.Typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                cast.characterName,
                minLines = 1,
                maxLines = 2,
                overflow = Ellipsis,
                style = TvTrackerTheme.Typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}
