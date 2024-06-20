package com.free.tvtracker.data.tracked.entities

data class StoredShowClientEntity(
    val tmdbId: Long,
    val title: String,
    var storedEpisodes: List<StoredEpisodeClientEntity>,
    val posterImage: String?,
    val backdropImage: String?,
    val status: String,
)
