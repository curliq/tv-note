package com.free.tvtracker.ui.watchlists.details.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.details.dialogs.DetailsSheetHeader
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsScreenNavAction
import com.free.tvtracker.ui.watchlists.details.WatchlistDetailsViewModel

@Composable
fun WatchlistDetailsRenameSheet(
    viewModel: WatchlistDetailsViewModel,
    navAction: (WatchlistDetailsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    val list = viewModel.loadContent.collectAsState().value
    if (list.watchlistId == -1) return
    TvTrackerTheme {
        Scaffold(modifier = Modifier.fillMaxWidth()) {
            WatchlistDetailsRenameSheetContent(
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
fun WatchlistDetailsRenameSheetContent(
    watchlistId: Int,
    watchlistName: String,
    navAction: (WatchlistDetailsScreenNavAction) -> Unit,
    action: (WatchlistDetailsViewModel.WatchlistDetailsAction) -> Unit,
    bottomPadding: Float = 0f
) {
    var text by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth()) {
        DetailsSheetHeader("Rename \"${watchlistName}\"")
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") },
            modifier = Modifier.padding(horizontal = sidePadding).fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                action(WatchlistDetailsViewModel.WatchlistDetailsAction.Rename(watchlistId, text))
                navAction(WatchlistDetailsScreenNavAction.HideBottomSheet)
            },
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.fillMaxWidth(0.5f).padding(horizontal = sidePadding).align(Alignment.End)
        ) {
            Text("Save")
        }
        Spacer(Modifier.height(bottomPadding.dp))
    }
}
