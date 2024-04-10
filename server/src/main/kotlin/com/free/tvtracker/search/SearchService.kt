package com.free.tvtracker.search

import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.TmdbClient
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse
import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.search.request.MediaType
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val logger: TvtrackerLogger,
    private val tmdbClient: TmdbClient,
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

    fun getShow(tmdbId: Int): TmdbShowBigResponse {
        val respEntity = tmdbClient.get(
            "/3/tv/$tmdbId}",
            TmdbShowBigResponse::class.java
        )
        return respEntity.body!!
    }
}


