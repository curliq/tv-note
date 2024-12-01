package com.free.tvtracker.previews

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.settings.login.LoginContent
import com.free.tvtracker.ui.settings.SettingsContent
import com.free.tvtracker.ui.settings.SettingsUiModel
import com.free.tvtracker.ui.settings.signup.SignupContent
import com.free.tvtracker.ui.settings.login.LoginViewModel
import com.free.tvtracker.ui.settings.signup.SignupViewModel

@Preview
@Composable
fun SettingsPreview() {
    val data = SettingsUiModel(
        pushNotificationEnabled = false,
        isAnon = true,
        personalInfo = SettingsUiModel.PersonalInfo(
            "username", "email"
        ),
        theme = SettingsUiModel.Theme.Dark
    )
    TvTrackerTheme {
        Scaffold { a ->
            SettingsContent(data, SnackbarHostState(), {}, { })
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    TvTrackerTheme {
        Scaffold { _ ->
            LoginContent(LoginViewModel.Result.Idle, {}, { })
        }
    }
}

@Preview
@Composable
fun SignupPreview() {
    TvTrackerTheme {
        Scaffold { _ ->
            SignupContent(SignupViewModel.Result.Idle, {}, { }, { true })
        }
    }
}

