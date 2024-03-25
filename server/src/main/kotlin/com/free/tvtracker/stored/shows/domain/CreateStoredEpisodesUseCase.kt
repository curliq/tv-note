package com.free.tvtracker.stored.shows.domain

import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.stored.shows.data.StoredEpisodeJdbcRepository
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.data.StoredShowEntity
import org.springframework.stereotype.Component

@Component
class CreateStoredEpisodesUseCase(private val storedEpisodeJdbcRepository: StoredEpisodeJdbcRepository) {

    operator fun invoke(
        tmdbShowResponse: TmdbShowBigResponse,
        storedShow: StoredShowEntity
    ): List<StoredEpisodeEntity> {
        val seasonBehindTheScenesNumber = 0
        val episodes = arrayListOf<StoredEpisodeEntity>()
        val seasons = tmdbShowResponse.seasons
            .filterNot { it.seasonNumber == seasonBehindTheScenesNumber }
            .map { it.seasonNumber!! to it.episodeCount!! }
        seasons.forEach { season ->
            (1..season.second).forEach { episodeNumber ->
                episodes.add(
                    StoredEpisodeEntity(
                        seasonNumber = season.first,
                        episodeNumber = episodeNumber,
                        storedShow = storedShow
                    )
                )
            }
        }
        storedEpisodeJdbcRepository.saveBatch(episodes)
        return episodes
    }
}
