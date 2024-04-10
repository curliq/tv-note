package com.free.tvtracker.screens.watching

import com.free.tvtracker.tracked.response.TrackedShowApiModel

class GetNextUnwatchedEpisodeUseCase {

    /**
     * @return id of stored episode
     */
    operator fun invoke(show: TrackedShowApiModel): TrackedShowApiModel.StoredEpisodeApiModel? {
        val shh =
            show.storedShow.storedEpisodes
                .filter { show.watchedEpisodes.map { it.storedEpisodeId }.contains(it.id) }
                .sortedWith(compareBy({ it.season }, { it.episode }))
                .lastOrNull()
                ?: return null
        val lastSeenIndex = show.storedShow.storedEpisodes.indexOfFirst { it.id == shh.id }
        return show.storedShow.storedEpisodes.getOrNull(lastSeenIndex + 1)
    }

}
