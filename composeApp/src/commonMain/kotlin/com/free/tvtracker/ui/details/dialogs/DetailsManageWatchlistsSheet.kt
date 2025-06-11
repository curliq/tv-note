package com.free.tvtracker.ui.details.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePaddingHalf
import com.free.tvtracker.ui.details.DetailsScreenNavAction
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.ui.details.DetailsUiState
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.details.DetailsViewModel.DetailsAction
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.FINISHED_LIST_ID

@Composable
fun DetailsManageWatchlistsSheet(
    viewModel: DetailsViewModel,
    navActions: (DetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val watchlists = viewModel.watchlists.collectAsState(DetailsViewModel.DetailsWatchlists(emptyList())).value
    val uiModel = viewModel.result.collectAsState().value as? DetailsUiState.Ok
    if (uiModel == null) return
    TvTrackerTheme {
        Scaffold {
            DetailsManageWatchlistsContent(uiModel.data, watchlists, viewModel::action, navActions, bottomPadding)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailsManageWatchlistsContent(
    uiModel: DetailsUiModel,
    watchlists: DetailsViewModel.DetailsWatchlists,
    action: (DetailsAction) -> Unit,
    navActions: (DetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        item {
            Text(
                "Lists",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = sidePadding, vertical = sidePadding).alpha(0.8f)
            )
        }
        items(watchlists.lists) { watchlist ->
            val isEnabled = watchlist.list.id != FINISHED_LIST_ID
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePaddingHalf)
                    .clip(TvTrackerTheme.ShapeButton)
                    .clickable(enabled = isEnabled, onClick = {
                        if (watchlist.checked) {
                            action.invoke(DetailsAction.RemoveFromWatchList(uiModel, watchlist.list.id))
                        } else {
                            action.invoke(DetailsAction.AddToWatchList(uiModel, watchlist.list.id))
                        }
                    })
            ) {
                Checkbox(watchlist.checked, { checked ->
                    if (checked) {
                        action.invoke(DetailsAction.AddToWatchList(uiModel, watchlist.list.id))
                    } else {
                        action.invoke(DetailsAction.RemoveFromWatchList(uiModel, watchlist.list.id))
                    }
                }, enabled = isEnabled)
                Spacer(modifier = Modifier.width(sidePaddingHalf))
                Text(watchlist.list.name, style = MaterialTheme.typography.bodyMediumEmphasized)
            }
        }
        if (uiModel.isWatching == true) {
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = sidePadding))
            }
            item {
                TextButton(
                    shape = TvTrackerTheme.ShapeButton,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = sidePadding),
                    onClick = {
                        action.invoke(
                            DetailsAction.TrackingAction(
                                uiModel,
                                DetailsUiModel.TrackingStatus.Action.RemoveFromWatching,
                                navActions
                            )
                        )
                    }
                ) {
                    Text(
                        text = "Remove from Watching",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp))
        }
    }
}
