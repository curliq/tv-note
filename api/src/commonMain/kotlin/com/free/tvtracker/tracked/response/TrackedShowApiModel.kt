package com.free.tvtracker.tracked.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackedShowApiModel(
    @SerialName("id") val id: Int,
    @SerialName("created_at_datetime") val createdAtDatetime: String,
    @SerialName("watched_episodes") val watchedEpisodes: List<WatchedEpisodeApiModel>,
    @SerialName("stored_show") val storedShow: StoredShowApiModel,
    @SerialName("watchlisted") val watchlisted: Boolean
) {
    @Serializable
    data class WatchedEpisodeApiModel(
        @SerialName("id") val id: Int,
        @SerialName("stored_episode_id") val storedEpisodeId: String,
    )

    @Serializable
    data class StoredShowApiModel(
        @SerialName("tmdb_id") val tmdbId: Int,
        @SerialName("title") val title: String,
        @SerialName("stored_episodes") val storedEpisodes: List<StoredEpisodeApiModel>,
        @SerialName("poster_image") val posterImage: String,
        @SerialName("status") val status: String,
    )

    @Serializable
    data class StoredEpisodeApiModel(
        @SerialName("id") val id: String,
        @SerialName("season") val season: Int,
        @SerialName("episode") val episode: Int,
        @SerialName("air_date") val airDate: String?,
    )
}
