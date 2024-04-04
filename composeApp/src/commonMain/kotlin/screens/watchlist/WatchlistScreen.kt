package besttvtracker.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import besttvtracker.TvTrackerTheme
import besttvtracker.shared.Greeting

@Composable
fun WatchlistScreen() {
    TvTrackerTheme {
        Box(Modifier.fillMaxWidth().fillMaxHeight().background(Color.Cyan)) {
            Text(Greeting().greet())
            LazyColumn {

            }
        }
    }
}
