package com.free.tvtracker.ui.settings

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.data.session.LocalPreferencesClientEntity
import com.free.tvtracker.data.session.SessionClientEntity

class SettingsUiModelMapper : MapperWithOptions<SessionClientEntity, SettingsUiModel, LocalPreferencesClientEntity> {
    override fun map(from: SessionClientEntity, options: LocalPreferencesClientEntity): SettingsUiModel {
        return SettingsUiModel(
            isAnon = from.isAnonymous,
            pushNotificationEnabled = from.preferencesPushAllowed,
            theme = mapTheme(options.theme),
            personalInfo = if (from.username != null) {
                SettingsUiModel.PersonalInfo(
                    username = from.username,
                    email = if (from.email.isNullOrEmpty()) null else from.email
                )
            } else {
                null
            }
        )
    }

    private fun mapTheme(theme: LocalPreferencesClientEntity.Theme): SettingsUiModel.Theme {
        return when (theme) {
            LocalPreferencesClientEntity.Theme.SystemDefault -> SettingsUiModel.Theme.System
            LocalPreferencesClientEntity.Theme.Light -> SettingsUiModel.Theme.Light
            LocalPreferencesClientEntity.Theme.Dark -> SettingsUiModel.Theme.Dark
        }
    }
}
