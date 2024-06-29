package com.free.tvtracker.storage.shows.domain

import com.free.tvtracker.storage.shows.data.StoredShowEntity
import com.free.tvtracker.storage.shows.data.StoredShowJpaRepository
import com.free.tvtracker.tmdb.data.TmdbShowBigResponse
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import kotlin.contracts.ExperimentalContracts

@Service
class StoredShowsService(
    private val storedShowJpaRepository: StoredShowJpaRepository,
    private val storedEpisodesService: StoredEpisodesService
) {

    fun getStoredShow(tmdbShowId: Int): StoredShowEntity? {
        val show = storedShowJpaRepository.findByTmdbId(tmdbShowId)
        if (isStoredShowCacheValid(show)) {
            return show
        }
        return null
    }

    fun createOrUpdateStoredShow(tmdbShowResponse: TmdbShowBigResponse): StoredShowEntity {
        val storedShow = storedShowJpaRepository.findByTmdbId(tmdbId = tmdbShowResponse.id)
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
            tmdbId = tmdbShowResponse.id,
            title = tmdbShowResponse.name ?: "",
            status = tmdbShowResponse.status ?: "",
            posterImage = tmdbShowResponse.posterPath ?: "",
            backdropImage = tmdbShowResponse.backdropPath ?: "",
        )
    }
}
