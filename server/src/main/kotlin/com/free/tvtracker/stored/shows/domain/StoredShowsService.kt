package com.free.tvtracker.stored.shows.domain

import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.search.SearchService
import com.free.tvtracker.stored.shows.data.StoredShowEntity
import com.free.tvtracker.stored.shows.data.StoredShowJpaRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
@Service
class StoredShowsService(
    private val storedShowJpaRepository: StoredShowJpaRepository,
    private val searchService: SearchService,
    private val storedEpisodesService: StoredEpisodesService
) {

    fun createOrUpdateStoredShow(tmdbShowId: Int): StoredShowEntity {
        val storedShow = storedShowJpaRepository.findByTmdbId(tmdbShowId)
        if (isStoredShowCacheValid(storedShow)) {
            return storedShow
        }
        val tmdbShowResponse = searchService.getShow(tmdbShowId)
        val newStoredShow = buildStoredShow(tmdbShowResponse)
        if (storedShow != null) {
            newStoredShow.createdAtDatetime = storedShow.createdAtDatetime
            newStoredShow.id = storedShow.id
        }
        storedShowJpaRepository.save(newStoredShow)
        val episodes = storedEpisodesService.getOrCreateEpisodes(tmdbShowResponse, newStoredShow)
        // assign episodes just for this instance to include them in the api response
        newStoredShow.storedEpisodes = episodes
        return newStoredShow
    }

    private fun isStoredShowCacheValid(storedShow: StoredShowEntity?): Boolean {
        contract { returns(true) implies (storedShow != null) }
        if (storedShow == null) {
            return false
        }
        val storedShowValidDuration = Duration.ofHours(24)
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
