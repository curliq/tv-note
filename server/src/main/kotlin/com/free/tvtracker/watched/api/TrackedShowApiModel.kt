package com.free.tvtracker.watched.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.watched.data.TrackedShowEntity

data class TrackedShowApiModel(
    @JsonProperty("id") val id: Int,
    @JsonProperty("created_at_datetime") val createdAtDatetime: String,
    @JsonProperty("watched_episodes_ids") val watchedEpisodeIds: List<Int>,
    @JsonProperty("stored_show") val storedShow: StoredShowApiModel,
    @JsonProperty("watchlisted") val watchlisted: Boolean
) {
    data class StoredShowApiModel(
        @JsonProperty("tmdb_id") val tmdbId: Int,
        @JsonProperty("title") val title: String,
        @JsonProperty("stored_episodes") val storedEpisodes: List<StoredEpisodeApiModel>,
        @JsonProperty("poster_image") val posterImage: String,
    )

    data class StoredEpisodeApiModel(
        @JsonProperty("id") val id: String,
        @JsonProperty("season") val season: Int,
        @JsonProperty("episode") val episode: Int,
    )
}

fun TrackedShowEntity.toApiModel(): TrackedShowApiModel {
    return TrackedShowApiModel(
        id = this.id,
        createdAtDatetime = this.createdAtDatetime,
        watchedEpisodeIds = this.watchedEpisodes.map { it.id },
        storedShow = this.storedShow.run {
            TrackedShowApiModel.StoredShowApiModel(
                tmdbId = tmdbId,
                title = title,
                storedEpisodes = storedEpisodes.map { ep ->
                    TrackedShowApiModel.StoredEpisodeApiModel(
                        id = ep.id.toString(),
                        season = ep.seasonNumber,
                        episode = ep.episodeNumber
                    )
                },
                posterImage = posterImage
            )
        },
        watchlisted = this.watchlisted
    )
}
