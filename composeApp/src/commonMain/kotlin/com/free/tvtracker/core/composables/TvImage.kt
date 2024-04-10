package com.free.tvtracker.core.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil3.compose.AsyncImage

@Composable
fun TvImage(imageUrl: String, modifier: Modifier= Modifier) {
    if (!LocalInspectionMode.current) { //not in @Preview, previews break with this
        AsyncImage(
            modifier = modifier,
            model = imageUrl,
            contentDescription = "content",
            contentScale = ContentScale.Crop
        )
    }
}

fun posterRatio() = 1f / 1.5f
