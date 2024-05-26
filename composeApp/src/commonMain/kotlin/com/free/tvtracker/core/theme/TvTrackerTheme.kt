package com.free.tvtracker.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.free.tvtracker.utils.OsPlatform
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TvTrackerTheme {
    val ShapeCornerMedium = 12.dp
    val FilledButtonHeight = 40.dp
    val sidePadding = 16.dp

    val ShapeButton = RoundedCornerShape(8.dp)

    val Typography = Typography(
        headlineMedium = Typography().headlineMedium.copy(fontWeight = FontWeight.Bold),
        headlineSmall = Typography().headlineSmall.copy(fontWeight = FontWeight.Bold),
        titleLarge = Typography().titleLarge.copy(fontWeight = FontWeight.Bold),
    )
}

@Composable
fun TvTrackerTheme(content: @Composable () -> Unit) {
    val colorsLight = lightColorScheme(
        background = if (OsPlatform().get() == OsPlatform.Platform.IOS) {
            Color.White
        } else {
            MaterialTheme.colorScheme.background
        },
    )
    val colorsDark = darkColorScheme()
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) colorsDark else colorsLight,
        typography = TvTrackerTheme.Typography,
        content = content,
    )
}
