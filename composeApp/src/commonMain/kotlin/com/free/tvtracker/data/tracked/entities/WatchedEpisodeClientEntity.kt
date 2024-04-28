package com.free.tvtracker.data.tracked.entities

data class WatchedEpisodeClientEntity(
    val id: String,
    val storedEpisodeId: Long,
    val trackedShowId: Long
) {
    companion object {
        fun fromSql(
            id: String,
            stored_episode_id: Long,
            tracked_show: Long
        ): WatchedEpisodeClientEntity {
            return WatchedEpisodeClientEntity(
                id, stored_episode_id, tracked_show
            )
        }
    }
}
