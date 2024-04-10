package com.free.tvtracker.data.tracked.entities

data class StoredEpisodeClientEntity(
    val id: String,
    val season: Long,
    val episode: Long,
) {
    companion object {
        fun fromSql(
            id: String,
            season: Long,
            episode: Long,
            storedShow: Long
        ): StoredEpisodeClientEntity {
            return StoredEpisodeClientEntity(
                id, season, episode
            )
        }
    }
}
