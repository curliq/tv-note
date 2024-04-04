package besttvtracker.screens.watching

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import besttvtracker.TvTrackerTheme
import besttvtracker.core.TvImage
import besttvtracker.core.Typography
import besttvtracker.shared.WatchingViewModel

data class WatchingItemUiModel(val title: String, val image: String, val latestEpisodeCode: String)

@Composable
fun WatchingScreen(nav: () -> Unit, viewModel: WatchingViewModel = remember { WatchingViewModel() }) {
    val shows = (0..20).map { WatchingItemUiModel("The Expanse", "", "Next: s3e9") }
    TvTrackerTheme {
        Column(Modifier.fillMaxWidth().fillMaxHeight().background(Color.Cyan)) {
            Text(viewModel.shows.collectAsState().value)
            LazyColumn {
                itemsIndexed(shows) { index, show ->
                    WatchingItem(
                        show,
                        onClick = nav,
                        onMarkWatched = {}
                    )
                }
            }
        }
    }
}

@Composable
fun WatchingItem(uiModel: WatchingItemUiModel, onClick: () -> Unit, onMarkWatched: () -> Unit) {
    Row(
        Modifier
            .height(IntrinsicSize.Max)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            Modifier
                .aspectRatio(1f)
                .border(1.dp, Color(13, 13, 13), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        ) {
            TvImage("https://w.forfun.com/fetch/54/54fa8b14c30aba474a19eedd1c145fd9.jpeg")
        }
        Column(Modifier.padding(start = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text(uiModel.title, maxLines = 1)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(uiModel.latestEpisodeCode)
                FilledTonalButton(
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = onMarkWatched,
                ) {
                    Text(text = "Mark watched", style = Typography.labelLarge)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
