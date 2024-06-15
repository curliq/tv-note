package com.free.tvtracker.ui.common.composables

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap


@Composable
fun LoadingIndicator(modifier: Modifier = Modifier, color: Color = ProgressIndicatorDefaults.circularColor) {
    CircularProgressIndicator(modifier = modifier, strokeCap = StrokeCap.Round, color = color)
}
