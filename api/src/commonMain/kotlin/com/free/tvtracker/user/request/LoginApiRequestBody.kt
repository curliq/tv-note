package com.free.tvtracker.user.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginApiRequestBody(
    val username: String,
    val password: String
)
