package com.free.tvtracker.stored.shows.domain

import com.free.tvtracker.core.tmdb.TmdbClient
import com.free.tvtracker.core.tmdb.data.TmdbSeasonResponse
import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.data.StoredEpisodeJdbcRepository
import com.free.tvtracker.stored.shows.data.StoredEpisodeJpaRepository
import com.free.tvtracker.stored.shows.data.StoredShowEntity
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Clock
import java.time.Duration
import java.time.LocalDate

@Service
class StoredEpisodesService(
    private val storedEpisodeJdbcRepository: StoredEpisodeJdbcRepository,
    private val storedEpisodeJpaRepository: StoredEpisodeJpaRepository,
    private val tmdbClient: TmdbClient,
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
                val seasonTmdb = getSeason(storedShow.tmdbId, season.first)
                (1..season.second).forEach { episodeNumber ->
                    val episodeTmdb =
                        seasonTmdb.episodes?.firstOrNull { episode -> episode.episodeNumber == episodeNumber }
                    if (episodeTmdb != null) {
                        episodes.add(
                            StoredEpisodeEntity(
                                id = episodeTmdb.id,
                                seasonNumber = season.first,
                                episodeNumber = episodeNumber,
                                storedShow = storedShow,
                                airDate = episodeTmdb.airDate,
                                episodeName = episodeTmdb.name,
                                thumbnail = episodeTmdb.stillPath
                            )
                        )
                    }
                }
            }
        }
        storedEpisodeJdbcRepository.saveBatch(episodes)
        return episodes.plus(storedShow.storedEpisodes).distinctBy { it.id }
            .sortedWith(compareBy({ it.seasonNumber }, { it.episodeNumber }))
    }

    fun getEpisodes(tmdbShowId: Int): List<StoredEpisodeEntity> {
        return storedEpisodeJpaRepository.findAllByStoredShowTmdbIdIs(tmdbShowId = tmdbShowId)
    }

    fun shouldFetchSeason(seasonNumber: Int, storedShow: StoredShowEntity): Boolean {
        val eps = storedShow.storedEpisodes.filter { it.seasonNumber == seasonNumber }
        val hasFutureEpisodes = eps.any { LocalDate.parse(it.airDate!!).isAfter(LocalDate.now(clock)) }
        val cacheExpired = expired(eps.lastOrNull()?.updatedAtDatetime)
        return eps.isEmpty() || (hasFutureEpisodes && cacheExpired)
    }

    private fun expired(episode: String?): Boolean {
        if (episode == null) return true
        val storedShowValidDuration = Duration.ofHours(24)
        return Timestamp.valueOf(episode).toInstant()
            .plus(storedShowValidDuration)
            .isAfter(clock.instant())
    }

    fun getSeason(tmdbShowId: Int, seasonNumber: Int): TmdbSeasonResponse {
        return tmdbClient.get(
            "/3/tv/$tmdbShowId/season/$seasonNumber",
            TmdbSeasonResponse::class.java,
        ).body!!
    }
}
