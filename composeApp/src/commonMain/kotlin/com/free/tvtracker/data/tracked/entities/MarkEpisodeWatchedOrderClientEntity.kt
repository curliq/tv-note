package com.free.tvtracker.data.tracked.entities

data class MarkEpisodeWatchedOrderClientEntity(
    val id: String,
    val showId: Long,
    val episodeId: String,
) {
    companion object {
        fun fromSql(
            id: String,
            show_id: Long,
            stored_episode_id: String
        ): MarkEpisodeWatchedOrderClientEntity =
            MarkEpisodeWatchedOrderClientEntity(
                id,
                show_id,
                stored_episode_id
            )
    }
}
