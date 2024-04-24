package com.free.tvtracker.stored.shows.domain

import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.search.SearchService
import com.free.tvtracker.stored.shows.data.StoredEpisodeJdbcRepository
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.data.StoredShowEntity
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDate

@Service
class StoredEpisodesService(
    private val storedEpisodeJdbcRepository: StoredEpisodeJdbcRepository,
    private val searchService: SearchService,
    private val clock: Clock
) {
    fun getOrCreateEpisodes(
        tmdbShowResponse: TmdbShowBigResponse,
        storedShow: StoredShowEntity
    ): List<StoredEpisodeEntity> {
        val seasonBehindTheScenesNumber = 0
        val episodes = arrayListOf<StoredEpisodeEntity>()
        val seasons = tmdbShowResponse.seasons
            .filterNot { it.seasonNumber == seasonBehindTheScenesNumber }
            .map { it.seasonNumber!! to it.episodeCount!! }
        seasons.forEach { season ->
            if (shouldFetchSeason(season.first, storedShow)) {
                val seasonTmdb = searchService.getSeason(storedShow.tmdbId, season.first)
                (1..season.second).forEach { episodeNumber ->
                    val episodeTmdb =
                        seasonTmdb.episodes?.firstOrNull { episode -> episode.episodeNumber == episodeNumber }
                    episodes.add(
                        StoredEpisodeEntity(
                            id = "${storedShow.id}_${season.first}_${episodeNumber}",
                            seasonNumber = season.first,
                            episodeNumber = episodeNumber,
                            storedShow = storedShow,
                            airDate = episodeTmdb?.airDate
                        )
                    )
                }
            }
        }
        storedEpisodeJdbcRepository.saveBatch(episodes)
        return episodes.plus(storedShow.storedEpisodes).distinctBy { it.id }
    }

    fun shouldFetchSeason(seasonNumber: Int, storedShow: StoredShowEntity): Boolean {
        val eps = storedShow.storedEpisodes.filter {it.seasonNumber == seasonNumber}
        return eps.isEmpty() || eps.any {
                LocalDate.parse(it.airDate!!).isAfter(LocalDate.now(clock))
        }
    }
}
