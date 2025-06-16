package com.free.tvtracker.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.logo_centered
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun TvImage(
    imageUrl: String,
    error: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier
) {
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
            CoilImage(
                modifier = modifier,
                failure = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        error?.invoke() ?: Image(
                            painter = painterResource(Res.drawable.logo_centered),
                            contentDescription = "Missing image",
                            modifier = Modifier.width(48.dp),
                        )
                    }
                },
                imageModel = {
                    imageUrl
                },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
            )
        } else {
            Box(modifier.background(Color(0xffe8776f)).fillMaxSize())
        }
    }
}

fun posterRatio() = 1f / 1.5f

fun backdropRatio() = 1.78f / 1f
