package besttvtracker.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImage

@Composable
fun TvImage(imageUrl: String) {
    if (LocalInspectionMode.current) {

    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = "content",
            contentScale = ContentScale.Crop
        )
    }
}
