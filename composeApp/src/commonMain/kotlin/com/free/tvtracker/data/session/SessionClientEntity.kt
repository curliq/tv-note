package com.free.tvtracker.data.session

data class SessionClientEntity(
    val token: String,
    val createdAtDatetime: String,
    val id: Long,
    val username: String?,
    val email: String?,
) {
    companion object {
        fun fromSql(
            token: String, createdAtDatetime: String, id: Long, username: String?, email: String?
        ): SessionClientEntity =
            SessionClientEntity(
                token = token,
                createdAtDatetime = createdAtDatetime,
                id = id,
                username = username,
                email = email,
            )
    }
}
