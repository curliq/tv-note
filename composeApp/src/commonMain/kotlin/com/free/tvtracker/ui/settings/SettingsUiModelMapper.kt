package com.free.tvtracker.ui.settings

import com.free.tvtracker.base.Mapper
import com.free.tvtracker.data.session.SessionClientEntity

class SettingsUiModelMapper : Mapper<SessionClientEntity, SettingsUiModel> {
    override fun map(from: SessionClientEntity): SettingsUiModel {
        return SettingsUiModel(
            isAnon = from.username == null,
            pushNotificationEnabled = from.preferencesPushAllowed,
            personalInfo = if (from.username != null) {
                SettingsUiModel.PersonalInfo(
                    username = from.username,
                    email = from.email
                )
            } else {
                null
            }
        )
    }
}
