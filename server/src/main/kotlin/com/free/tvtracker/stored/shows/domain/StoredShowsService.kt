package com.free.tvtracker.stored.shows.domain

import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.search.SearchService
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.data.StoredEpisodeJpaRepository
import com.free.tvtracker.stored.shows.data.StoredShowEntity
import com.free.tvtracker.stored.shows.data.StoredShowJpaRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant

@Service
class StoredShowsService(
    private val storedShowJpaRepository: StoredShowJpaRepository,
    private val storedEpisodeJpaRepository: StoredEpisodeJpaRepository,
    private val searchService: SearchService,
    private val createStoredEpisodesUseCase: CreateStoredEpisodesUseCase
) {

    fun createStoredShow(tmdbShowId: Int): Pair<StoredShowEntity, List<StoredEpisodeEntity>> {
        val storedShow = storedShowJpaRepository.findByTmdbId(tmdbShowId)
        if (storedShow != null && storedShowStillGood(storedShow)) {
            val eps = storedEpisodeJpaRepository.findAllByStoredShowIdIs(storedShow.id)
            return storedShow to eps
        }
        val tmdbShowResponse = searchService.getShow(tmdbShowId)
        val newStoredShow = buildStoredShow(tmdbShowResponse)
        if (storedShow != null) {
            newStoredShow.createdAtDatetime = storedShow.createdAtDatetime
            newStoredShow.id = storedShow.id
        }
        storedShowJpaRepository.save(newStoredShow)
        val episodes = createStoredEpisodesUseCase(tmdbShowResponse, newStoredShow)
        return newStoredShow to episodes
    }

    private fun storedShowStillGood(storedShow: StoredShowEntity): Boolean {
        val storedShowValidDuration = Duration.ofMinutes(30)
        return Timestamp.valueOf(storedShow.updatedAtDatetime).toInstant()
            .plus(storedShowValidDuration)
            .isAfter(Instant.now())
    }

    private fun buildStoredShow(tmdbShowResponse: TmdbShowBigResponse): StoredShowEntity {
        return StoredShowEntity(
            tmdbId = tmdbShowResponse.id!!,
            title = tmdbShowResponse.name ?: "",
            status = tmdbShowResponse.status ?: "",
            posterImage = tmdbShowResponse.posterPath ?: "",
        )
    }
}
