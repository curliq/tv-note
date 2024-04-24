package com.free.tvtracker.data.tracked.entities

data class StoredEpisodeClientEntity(
    val id: String,
    val season: Long,
    val episode: Long,
    val airDate: String?
) {
    companion object {
        fun fromSql(
            id: String,
            season: Long,
            episode: Long,
            storedShow: Long,
            airDate: String?
        ): StoredEpisodeClientEntity {
            return StoredEpisodeClientEntity(
                id, season, episode, airDate
            )
        }
    }
}
