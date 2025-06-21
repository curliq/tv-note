package com.free.tvtracker.features.tracked.data.shows;

data class EpisodesReleaseTodayQueryResult(
    val showTitle: String,
    val showId: Int,
    val userFcmToken: String
)
