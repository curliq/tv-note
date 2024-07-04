package com.free.tvtracker.ui.discover.dialogs

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.TvImage
import com.free.tvtracker.ui.common.composables.gridInLazyColumn
import com.free.tvtracker.ui.common.composables.posterRatio
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.discover.DiscoverScreenNavActions
import com.free.tvtracker.ui.discover.DiscoverUiModel.Content
import com.free.tvtracker.ui.discover.DiscoverUiState
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel.DiscoverViewModelAction

@Composable
fun DiscoverTrendingSheet(
    viewModel: DiscoverViewModel,
    navActions: (DiscoverScreenNavActions) -> Unit,
    bottomPadding: Float = 0f
) {
    val show = viewModel.data.collectAsState().value as DiscoverUiState.Ok
    TvTrackerTheme {
        DiscoverTrendingSheetContent(
            show.uiModel.contentTrendingWeekly.data,
            navActions,
            { viewModel.action(DiscoverViewModelAction.LoadPageTrending) },
            bottomPadding
        )
    }
}

@Composable
fun DiscoverTrendingSheetContent(
    data: List<Content>,
    navAction: (DiscoverScreenNavActions) -> Unit,
    loadPageAction: () -> Unit,
    bottomPadding: Float = 0f
) {
    val listState = rememberLazyListState()
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            loadPageAction()
        }
    }
    LazyColumn(state = listState) {
        item {
            Spacer(modifier = Modifier.height(sidePadding))
        }
        gridInLazyColumn(
            rowModifier = Modifier.fillMaxWidth().padding(horizontal = sidePadding),
            columns = 3,
            itemCount = data.size
        ) { index ->
            val content = data[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                onClick = { navAction(DiscoverScreenNavActions.GoShowDetails(content.tmdbId, content.isTvShow)) },
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
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp + sidePadding))
        }
    }
}
