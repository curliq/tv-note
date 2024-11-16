package com.free.tvtracker.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import besttvtracker.composeapp.generated.resources.Res
import com.free.tvtracker.ui.settings.SettingsUiModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * https://proandroiddev.com/custom-font-magic-in-compose-multiplatform-unlock-your-creativity-dcd0c9fa7756
 */
private val cache: MutableMap<String, Font> = mutableMapOf()

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font {
    return cache.getOrPut(res) {
        val byteArray = runBlocking {
            Res.readBytes("font/$res.ttf")
        }
        Font(res, byteArray, weight, style)
    }
}

@Composable
actual fun themePreferences(): SettingsUiModel.Theme? {
    return SettingsUiModel.Theme.Light
}
