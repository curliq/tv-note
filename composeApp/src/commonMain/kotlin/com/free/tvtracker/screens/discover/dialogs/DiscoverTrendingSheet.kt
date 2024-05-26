package com.free.tvtracker.screens.discover.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.NonLazyGrid
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.composables.gridInLazyColumn
import com.free.tvtracker.core.composables.posterRatio
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.screens.discover.DiscoverScreenNavActions
import com.free.tvtracker.screens.discover.DiscoverUiModel.Content
import com.free.tvtracker.screens.discover.DiscoverUiState
import com.free.tvtracker.screens.discover.DiscoverViewModel

@Composable
fun DiscoverTrendingSheet(
    viewModel: DiscoverViewModel,
    navActions: (DiscoverScreenNavActions) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.uiModel.collectAsState().value as DiscoverUiState.Ok
    TvTrackerTheme {
        DiscoverTrendingSheetContent(show.uiModel.showsTrendingWeekly, navActions, bottomPadding)
    }
}

@Composable
fun DiscoverTrendingSheetContent(
    content: List<Content>,
    action: (DiscoverScreenNavActions) -> Unit,
    bottomPadding: Float = 0f
) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(sidePadding))
        }
        gridInLazyColumn(
            rowModifier = Modifier.fillMaxWidth().padding(horizontal = sidePadding),
            columns = 3,
            itemCount = content.size
        ) { index ->
            val content = content[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                onClick = { action(DiscoverScreenNavActions.GoShowDetails(content.tmdbId)) },
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(Modifier.aspectRatio(posterRatio())) {
                    TvImage(content.image, modifier = Modifier.fillMaxSize())
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.padding(8.dp)) {
                    Text(
                        content.title,
                        minLines = 1,
                        maxLines = 3,
                        overflow = Ellipsis,
                        style = TvTrackerTheme.Typography.labelMedium
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp + sidePadding))
        }
    }
}
