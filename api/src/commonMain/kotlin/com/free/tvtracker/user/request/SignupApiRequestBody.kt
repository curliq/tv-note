package com.free.tvtracker.user.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupApiRequestBody(
    val username: String,
    val password: String,
    val email: String? = null
)
