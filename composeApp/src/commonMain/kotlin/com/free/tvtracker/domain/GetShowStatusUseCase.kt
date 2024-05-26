package com.free.tvtracker.domain

import com.free.tvtracker.constants.TmdbShowStatus

class GetShowStatusUseCase {
    operator fun invoke(
        tmdbShowStatus: String?,
        firstEpisodeAirDateTime: String?,
        lastEpisodeAirDateTime: String?
    ): String {
        val ongoing = when (TmdbShowStatus.entries.firstOrNull { it.status == tmdbShowStatus }) {
            TmdbShowStatus.ENDED -> lastEpisodeAirDateTime?.substring(0, 4) + " (Ended)"
            TmdbShowStatus.RETURNING -> "Ongoing"
            TmdbShowStatus.CANCELED -> lastEpisodeAirDateTime?.substring(0, 4) + " (Canceled)"
            null -> ""
        }
        val airDate = try {
            firstEpisodeAirDateTime?.substring(0, 4)
        } catch (e: Exception) {
            "Unknown air date"
        }
        return "$airDate - $ongoing"
    }
}
