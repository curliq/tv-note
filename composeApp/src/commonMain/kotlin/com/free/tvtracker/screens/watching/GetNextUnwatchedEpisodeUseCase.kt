package com.free.tvtracker.screens.watching

import com.free.tvtracker.tracked.response.TrackedShowApiModel

class GetNextUnwatchedEpisodeUseCase {

    /**
     * @return id of stored episode
     */
    operator fun invoke(show: TrackedShowApiModel): TrackedShowApiModel.StoredEpisodeApiModel? {
        val lastWatchedEpisode =
            show.storedShow.storedEpisodes
                .filter { show.watchedEpisodes.map { it.storedEpisodeId }.contains(it.id) }
                .sortedWith(compareBy({ it.season }, { it.episode }))
                .lastOrNull()
        val nextUnseenIndex = if (lastWatchedEpisode == null) {
            0
        } else {
            show.storedShow.storedEpisodes.indexOfFirst { it.id == lastWatchedEpisode.id } + 1
        }
        return show.storedShow.storedEpisodes.getOrNull(nextUnseenIndex)
    }

}
