package com.free.tvtracker.data.tracked.entities

data class StoredEpisodeClientEntity(
    val id: Long,
    val season: Long,
    val episode: Long,
    val airDate: String?
) {
    companion object {
        fun fromSql(
            id: Long,
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
