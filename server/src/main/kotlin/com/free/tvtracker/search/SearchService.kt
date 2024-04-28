package com.free.tvtracker.search

import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.TmdbClient
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse
import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.search.request.MediaType
import com.free.tvtracker.stored.shows.data.StoredEpisodeEntity
import com.free.tvtracker.stored.shows.domain.StoredEpisodesService
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val logger: TvtrackerLogger,
    private val tmdbClient: TmdbClient,
    private val storedEpisodesService: StoredEpisodesService,
) {
    fun searchTerm(term: String, mediaType: MediaType): TmdbSearchMultiResponse {
        val media = when (mediaType) {
            MediaType.ALL -> "multi"
            MediaType.TV_SHOWS -> "tv"
            MediaType.MOVIES -> "movie"
            MediaType.PEOPLE -> "person"
        }
        val respEntity = tmdbClient.get(
            "/3/search/$media",
            TmdbSearchMultiResponse::class.java,
            mapOf("query" to term)
        )
        return respEntity.body!!
    }

    fun getShow(tmdbShowId: Int): TmdbShowBigResponse {
        val respEntity = tmdbClient.get(
            "/3/tv/$tmdbShowId}",
            TmdbShowBigResponse::class.java,
            params = mapOf(
                "append_to_response" to "credits,watch/providers,videos,images"
            )
        )
        return respEntity.body!!
    }

    fun getShowApiModel(tmdbShowId: Int): TmdbShowDetailsApiModel {
        val episodes = storedEpisodesService.getEpisodes(tmdbShowId = tmdbShowId)
        return getShow(tmdbShowId).toApiModel(episodes.map { it.toApiModel() })
    }

    fun StoredEpisodeEntity.toApiModel(): TmdbShowDetailsApiModel.Season.Episode {
        return TmdbShowDetailsApiModel.Season.Episode(
            id = this.id,
            number = this.episodeNumber,
            thumbnail = this.thumbnail,
            name = this.episodeName,
            airDate = this.airDate,
            seasonNumber = this.seasonNumber,
        )
    }
}


