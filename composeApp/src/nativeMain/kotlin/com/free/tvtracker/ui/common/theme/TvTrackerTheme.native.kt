package com.free.tvtracker.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.free.tvtracker.ui.settings.SettingsUiModel

/**
 * https://proandroiddev.com/custom-font-magic-in-compose-multiplatform-unlock-your-creativity-dcd0c9fa7756
 */
@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font {
    return Font(0, weight, style)
}

@Composable
actual fun themePreferences(): SettingsUiModel.Theme? { return null
}
