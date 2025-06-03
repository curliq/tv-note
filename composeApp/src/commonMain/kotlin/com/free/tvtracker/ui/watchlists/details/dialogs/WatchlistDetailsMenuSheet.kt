package com.free.tvtracker.ui.watchlists.details.dialogs

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_delete_account
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePaddingHalf
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsScreenNavAction
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsUiState
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsViewModel
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.FINISHED_LIST_ID
import com.free.tvtracker.watchlists.response.WatchlistApiModel.Companion.WATCHLIST_LIST_ID

@Composable
fun WatchlistDetailsMenuSheet(
    viewModel: WatchlistDetailsViewModel,
    navAction: (WatchlistDetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val list = viewModel.shows.collectAsState().value as? WatchlistDetailsUiState.Ok
    if (list == null) return
    TvTrackerTheme {
        Scaffold {
            WatchlistDetailsMenuSheetContent(
                list.watchlistId,
                list.watchlistName,
                navAction,
                viewModel::action,
                bottomPadding
            )
        }
    }
}

@Composable
fun WatchlistDetailsMenuSheetContent(
    watchlistId: Int,
    watchlistName: String,
    navAction: (WatchlistDetailsScreenNavAction) -> Unit,
    action: (WatchlistDetailsViewModel.WatchlistDetailsAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val enabled = watchlistId !in listOf(WATCHLIST_LIST_ID, FINISHED_LIST_ID)
    Column {
        TextButton(
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf).fillMaxWidth(),
            contentPadding = PaddingValues(TvTrackerTheme.sidePaddingHalf),
            enabled = enabled,
            onClick = {
                navAction(WatchlistDetailsScreenNavAction.ShowRenameDialog(watchlistId, watchlistName))
            }
        ) {
            Icon(Icons.Rounded.Edit, "", tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(text = "Rename")
            Spacer(modifier = Modifier.weight(1f))
        }
        val showDeleteConfirm = remember { mutableStateOf(false) }

        Crossfade(targetState = showDeleteConfirm.value) {
            if (it) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = sidePadding)
                ) {
                    Text(
                        text = "Are you sure?",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        shape = TvTrackerTheme.ShapeButton,
                        onClick = { showDeleteConfirm.value = false }) {
                        Text(text = "No")
                    }
                    TextButton(
                        shape = TvTrackerTheme.ShapeButton,
                        onClick = {
                            action(WatchlistDetailsViewModel.WatchlistDetailsAction.Delete(watchlistId))
                            navAction(WatchlistDetailsScreenNavAction.OnDelete)
                        }) {
                        Text(
                            text = "Yes",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else {
                Row {
                    TextButton(
                        shape = TvTrackerTheme.ShapeButton,
                        modifier = Modifier.padding(horizontal = sidePaddingHalf).fillMaxWidth(),
                        contentPadding = PaddingValues(sidePaddingHalf),
                        enabled = enabled,
                        onClick = { showDeleteConfirm.value = true }
                    ) {
                        ResImage(
                            Res.drawable.ic_delete_account,
                            "Rename",
                            tint = if (enabled) MaterialTheme.colorScheme.error else null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Delete \"${watchlistName}\"",
                            color = if (enabled) MaterialTheme.colorScheme.error else Color.Unspecified
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(Modifier.height(bottomPadding.dp))
    }
}
