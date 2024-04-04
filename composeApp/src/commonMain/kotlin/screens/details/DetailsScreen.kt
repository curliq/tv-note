package besttvtracker.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DetailsScreen() {
    Column(Modifier.fillMaxWidth().background(Color.Green), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
        }) {
            Text("Click me!")
        }
    }
}
