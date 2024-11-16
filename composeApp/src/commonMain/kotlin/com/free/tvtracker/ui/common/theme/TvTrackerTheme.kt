package com.free.tvtracker.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.free.tvtracker.expect.OsPlatform
import com.free.tvtracker.ui.settings.SettingsUiModel

/**
 * https://proandroiddev.com/custom-font-magic-in-compose-multiplatform-unlock-your-creativity-dcd0c9fa7756
 */
@Composable
expect fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font

object TvTrackerTheme {
    val ShapeCornerMedium = 12.dp
    val FilledButtonHeight = 40.dp
    val sidePadding = 16.dp
    val sidePaddingHalf = 8.dp

    val ShapeButton = RoundedCornerShape(8.dp)
}

@Composable
expect fun themePreferences(): SettingsUiModel.Theme?

@Composable
fun TvTrackerTheme(themePrefs: SettingsUiModel.Theme? = themePreferences(), content: @Composable () -> Unit) {

    val colorsLight = lightColorScheme().run {
        if (OsPlatform().get() == OsPlatform.Platform.IOS) {
            this.copy(
                background = Color.White,
                surfaceContainerLow = Color.White // sticky header background on sheets
            )
        } else {
            this
        }
    }

    val colorsDark = darkColorScheme()

    val ibmNormal = FontFamily(
        font("IBMPlexSans", "ibmplexsans_regular", FontWeight.Normal, FontStyle.Normal)
    )
    val ibmSemiBold = FontFamily(
        font("IBMPlexSans", "ibmplexsans_semibold", FontWeight.SemiBold, FontStyle.Normal)
    )
    val ibmBold = FontFamily(
        font("IBMPlexSans", "ibmplexsans_bold", FontWeight.Bold, FontStyle.Normal)
    )
    val Typography = Typography(
        displayLarge = Typography().displayLarge.copy(fontFamily = ibmNormal),
        displayMedium = Typography().displayMedium.copy(fontFamily = ibmNormal),
        displaySmall = Typography().displaySmall.copy(fontFamily = ibmNormal),
        headlineLarge = Typography().headlineLarge.copy(fontFamily = ibmBold),
        headlineMedium = Typography().headlineMedium.copy(fontFamily = ibmBold),
        headlineSmall = Typography().headlineSmall.copy(fontFamily = ibmBold),
        titleLarge = Typography().titleLarge.copy(fontFamily = ibmBold),
        titleMedium = Typography().titleMedium.copy(fontFamily = ibmBold),
        titleSmall = Typography().titleSmall.copy(fontFamily = ibmBold),
        bodyLarge = Typography().bodyLarge.copy(fontFamily = ibmNormal),
        bodyMedium = Typography().bodyMedium.copy(fontFamily = ibmNormal),
        bodySmall = Typography().bodySmall.copy(fontFamily = ibmNormal),
        labelLarge = Typography().labelLarge.copy(fontFamily = ibmSemiBold),
        labelMedium = Typography().labelMedium.copy(fontFamily = ibmSemiBold),
        labelSmall = Typography().labelSmall.copy(fontFamily = ibmSemiBold),
    )
    val theme = when (themePrefs) {
        SettingsUiModel.Theme.System, null -> if (isSystemInDarkTheme()) colorsDark else colorsLight
        SettingsUiModel.Theme.Dark -> colorsDark
        SettingsUiModel.Theme.Light -> colorsLight
    }
    MaterialTheme(
        colorScheme = theme,
        typography = Typography,
        content = content,
    )
}
