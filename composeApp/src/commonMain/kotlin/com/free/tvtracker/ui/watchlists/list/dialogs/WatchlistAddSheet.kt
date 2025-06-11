package com.free.tvtracker.ui.watchlists.list.dialogs

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
import com.free.tvtracker.ui.watchlists.list.WatchlistsScreenNavAction
import com.free.tvtracker.ui.watchlists.list.WatchlistsViewModel

@Composable
fun WatchlistAddSheet(
    viewModel: WatchlistsViewModel,
    navAction: (WatchlistsScreenNavAction) -> Unit,
    bottomPadding: Float = 0f
) {
    TvTrackerTheme {
        Scaffold(modifier = Modifier.fillMaxWidth()) {
            WatchlistAddSheetContent(
                navAction,
                viewModel::action,
                bottomPadding
            )
        }
    }
}

@Composable
fun WatchlistAddSheetContent(
    navAction: (WatchlistsScreenNavAction) -> Unit,
    action: (WatchlistsViewModel.WatchlistsAction) -> Unit,
    bottomPadding: Float = 0f
) {
    var text by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth()) {
        DetailsSheetHeader("Create new watchlist")
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
                if (text.isNotEmpty()) {
                    navAction(WatchlistsScreenNavAction.HideBottomSheet)
                    action(WatchlistsViewModel.WatchlistsAction.New(text))
                }
            },
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.fillMaxWidth(0.5f).padding(horizontal = sidePadding).align(Alignment.End)
        ) {
            Text("Create")
        }
        Spacer(Modifier.height(bottomPadding.dp))
    }
}
