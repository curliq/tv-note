package com.free.tvtracker.data.session

data class LocalPreferencesClientEntity(
    val welcomeComplete: Boolean,
) {
    companion object {
        fun fromSql(
            welcomeComplete: Boolean?
        ): LocalPreferencesClientEntity =
            LocalPreferencesClientEntity(
                welcomeComplete = welcomeComplete ?: false
            )
    }
}
