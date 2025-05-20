package com.free.tvtracker.ui.details.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.free.tvtracker.ui.details.DetailsUiModel
import com.free.tvtracker.ui.details.DetailsUiState
import com.free.tvtracker.ui.details.DetailsViewModel

@Composable
fun DetailsReviewsSheet(
    viewModel: DetailsViewModel,
    bottomPadding: Float = 0f
) {
    val show = viewModel.result.collectAsState().value as DetailsUiState.Ok
    TvTrackerTheme {
        Scaffold {
            DetailsReviewsSheetContent(show.data, bottomPadding)
        }
    }
}

@Composable
fun DetailsReviewsSheetContent(
    show: DetailsUiModel,
    bottomPadding: Float = 0f
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        item {
            Column {
                Text(
                    "Reviews",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding, vertical = 8.dp)
                )
                Text(
                    "from IMDB (${show.reviews?.total ?: 0})",
                    modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
            }
        }
        items(show.reviews?.reviews ?: emptyList()) {
            ReviewCard(
                it,
                modifier = Modifier.padding(
                    start = sidePadding,
                    end = sidePadding,
                    top = sidePadding,
                    bottom = sidePadding
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(bottomPadding.dp))
        }
    }
}

@Composable
private fun ReviewCard(review: DetailsUiModel.Reviews.Review, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = "author",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(sidePadding))
        Column {
            Text(review.authorName, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            Text("\"${review.title}\"", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(review.content)
        }
    }
}
