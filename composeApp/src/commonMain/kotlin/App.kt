package besttvtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TvTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Composable
fun WatchingScreen(nav: () -> Unit) {
    TvTrackerTheme {
        Box(Modifier.fillMaxWidth().fillMaxHeight().background(Color.Cyan)) {
            Text("watching")
            Button(onClick = {
                nav()
            }) {
                Text("Click me!")
            }
        }
    }
}

@Composable
fun FinishedScreen(click: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Color.Green), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            click()
        }) {
            Text("Click me!")
        }
    }
}

@Composable
fun WatchlistScreen() {
    TvTrackerTheme {
        Text("watchlist")
    }
}

@Composable
fun DiscoverScreen() {
    TvTrackerTheme {
        Text("discover")
    }
}

@Composable
fun SettingsScreen() {
    TvTrackerTheme {
        Text("settings")
    }
}

@Composable
fun ShowDetailsScreen() {
    TvTrackerTheme {
        Box(Modifier.fillMaxWidth().fillMaxHeight().background(Color.Green)) {

            Text("details")
        }
    }
}

@Preview
@Composable
fun test() {
    ShowDetailsScreen()
}

