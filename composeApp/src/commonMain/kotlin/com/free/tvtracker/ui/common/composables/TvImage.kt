package com.free.tvtracker.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@Composable
fun TvImage(imageUrl: String,  modifier: Modifier = Modifier, containerModifier: Modifier = Modifier) {
    Box(
        containerModifier.then(
            Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium)
            ).clip(RoundedCornerShape(TvTrackerTheme.ShapeCornerMedium))
        )
    ) {
        if (!LocalInspectionMode.current) { //not in @Preview, previews break with this
            AsyncImage(
                modifier = modifier,
                model = imageUrl,
                contentDescription = "content",
                contentScale = ContentScale.Crop
            )
        } else {
            Box(modifier.background(Color(0xffe8776f)).fillMaxSize())
        }
    }
}

fun posterRatio() = 1f / 1.5f

fun backdropRatio() = 1.78f / 1f
