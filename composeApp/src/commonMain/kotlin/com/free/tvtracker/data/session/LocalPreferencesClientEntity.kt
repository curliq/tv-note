package com.free.tvtracker.data.session

data class LocalPreferencesClientEntity(
    val welcomeComplete: Boolean,
    val theme: Theme,
    val purchasedApp: Boolean
) {

    enum class Theme(val value: Int) { SystemDefault(0), Light(1), Dark(2), }

    companion object {
        fun fromSql(
            localId: Long,
            welcomeComplete: Boolean?,
            purchasedApp: Boolean?,
            theme: Long?,
        ): LocalPreferencesClientEntity =
            LocalPreferencesClientEntity(
                welcomeComplete = welcomeComplete ?: false,
                theme = Theme.entries.firstOrNull { it.value == theme?.toInt() } ?: Theme.SystemDefault,
                purchasedApp = purchasedApp ?: false
            )
    }
}
