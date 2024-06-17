package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.settings.SettingsContent
import com.free.tvtracker.ui.settings.SettingsUiModel

@Preview
@Composable
fun SettingsPreview() {
    val data = SettingsUiModel(
        pushNotificationEnabled = false,
        isAnon = true,
        personalInfo = SettingsUiModel.PersonalInfo(
            "username", "email"
        )
    )
    TvTrackerTheme {
        Scaffold { a ->
            SettingsContent(data, { })
        }
    }
}
