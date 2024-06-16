package com.free.tvtracker.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

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
    val colorsLight = lightColorScheme()
    val colorsDark = darkColorScheme()
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) colorsDark else colorsLight,
        typography = TvTrackerTheme.Typography,
        content = content,
    )
}