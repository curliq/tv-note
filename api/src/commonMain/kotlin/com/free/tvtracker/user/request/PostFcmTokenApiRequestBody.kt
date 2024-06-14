package com.free.tvtracker.user.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostFcmTokenApiRequestBody(@SerialName("fcm_token") val fcmToken: String)
