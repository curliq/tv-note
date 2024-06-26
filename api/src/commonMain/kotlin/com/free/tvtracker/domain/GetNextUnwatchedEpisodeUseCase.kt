package com.free.tvtracker.domain

import com.free.tvtracker.tracked.response.TrackedContentApiModel

class GetNextUnwatchedEpisodeUseCase {

    /**
     * @return id of stored episode
     */
    operator fun invoke(show: TrackedContentApiModel): TrackedContentApiModel.TvShow.StoredEpisodeApiModel? {
        val lastWatchedEpisode =
            show.tvShow!!.storedShow.storedEpisodes
                .filter { show.tvShow.watchedEpisodes.map { it.storedEpisodeId }.contains(it.id) }
                .sortedWith(compareBy({ it.season }, { it.episode }))
                .lastOrNull()
        val nextUnseenIndex = if (lastWatchedEpisode == null) {
            0
        } else {
            show.tvShow.storedShow.storedEpisodes.indexOfFirst { it.id == lastWatchedEpisode.id } + 1
        }
        return show.tvShow.storedShow.storedEpisodes.getOrNull(nextUnseenIndex)
    }

}
