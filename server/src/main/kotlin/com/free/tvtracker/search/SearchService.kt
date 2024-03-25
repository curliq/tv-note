package com.free.tvtracker.search

import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.TmdbClient
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val logger: TvtrackerLogger,
    private val tmdbClient: TmdbClient,
) {
    fun searchByTerm(term: String): TmdbSearchMultiResponse {
        val respEntity = tmdbClient.get(
            "/3/search/multi",
            TmdbSearchMultiResponse::class.java,
            mapOf("query" to term)
        )
        return respEntity.body!!
    }

}


