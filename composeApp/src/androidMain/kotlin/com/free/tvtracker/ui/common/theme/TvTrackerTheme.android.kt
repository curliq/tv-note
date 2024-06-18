package com.free.tvtracker.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.free.tvtracker.ui.settings.SettingsUiModel
import com.free.tvtracker.ui.settings.SettingsUiState
import com.free.tvtracker.ui.settings.SettingsViewModel
import org.koin.androidx.compose.get

@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(res, "font", context.packageName)
    return Font(id, weight, style)
}

@Composable
actual fun themePreferences(): SettingsUiModel.Theme? {
    if (!LocalInspectionMode.current) { //not in @Preview, previews break with this
        val settingsViewModel: SettingsViewModel = get()
        return settingsViewModel.theme.collectAsState(null).value
    } else {
        return null
    }
}
