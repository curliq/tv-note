package com.free.tvtracker.user.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdatePreferencesApiRequestBody(
    @SerialName("push_prefs_allowed")
    val pushPrefsAllowed: Boolean? = null
)
