package com.free.tvtracker.data.tracked.entities

data class WatchedEpisodeClientEntity(
    val id: Long,
    val storedEpisodeId: String,
) {
    companion object {
        fun fromSql(
            id: Long,
            stored_episode_id: String,
            tracked_show: Long?
        ): WatchedEpisodeClientEntity {
            return WatchedEpisodeClientEntity(
                id, stored_episode_id
            )
        }
    }
}
