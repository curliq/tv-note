package com.free.tvtracker.data.tracked.entities

data class TrackedShowClientEntity(
    val id: Long,
    val createdAtDatetime: String,
    var watchedEpisodes: List<WatchedEpisodeClientEntity>,
    var storedShow: StoredShowClientEntity,
    val watchlisted: Boolean
) {
    companion object {
        fun fromSql(
            id: Long,
            createdAtDatetime: String?,
            storedShow: Long?,
            watchlisted: Boolean?,
            tmdbId: Long,
            title: String,
            posterImage: String,
            status: String,
        ): TrackedShowClientEntity {
            return TrackedShowClientEntity(
                id = id,
                createdAtDatetime = createdAtDatetime!!,
                watchedEpisodes = emptyList(),
                storedShow = StoredShowClientEntity(
                    tmdbId = tmdbId,
                    title = title,
                    storedEpisodes = emptyList(),
                    posterImage = posterImage,
                    status = status,
                ),
                watchlisted = watchlisted!!,
            )
        }
    }
}
