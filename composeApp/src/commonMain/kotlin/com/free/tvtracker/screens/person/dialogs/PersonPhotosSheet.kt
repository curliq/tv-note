package com.free.tvtracker.screens.person.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.composables.TvImage
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.screens.details.dialogs.DetailsSheetHeader
import com.free.tvtracker.screens.person.PersonUiModel
import com.free.tvtracker.screens.person.PersonUiState
import com.free.tvtracker.screens.person.PersonViewModel

@Composable
fun PersonPhotosSheet(
    viewModel: PersonViewModel,
    bottomPadding: Float = 0f
) {
    val show = viewModel.result.collectAsState().value as PersonUiState.Ok
    TvTrackerTheme {
        PersonPhotosContent(show.data, bottomPadding)
    }
}

@Composable
fun PersonPhotosContent(
    person: PersonUiModel,
    bottomPadding: Float = 0f
) {
    Column {
        DetailsSheetHeader("Photos")
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(sidePadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(person.photos) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box(Modifier.aspectRatio(1f)) {
                        TvImage(item, modifier = Modifier.fillMaxSize())
                    }
                }
            }
            item(span = { GridItemSpan(3) }) {
                Spacer(modifier = Modifier.height(bottomPadding.dp))
            }
        }
    }
}
