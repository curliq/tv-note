package com.free.tvtracker.data.session

data class SessionClientEntity(
    val token: String,
    val createdAtDatetime: String,
    val userId: Long,
    val username: String?,
    val email: String?,
    val preferencesPushAllowed: Boolean
) {
    companion object {
        fun fromSql(
            localSessionId: Long,
            token: String,
            createdAtDatetime: String,
            userId: Long,
            username: String?,
            email: String?,
            preferencesPushAllowed: Boolean?
        ): SessionClientEntity =
            SessionClientEntity(
                token = token,
                createdAtDatetime = createdAtDatetime,
                userId = userId,
                username = username,
                email = email,
                preferencesPushAllowed = preferencesPushAllowed == true
            )
    }
}
