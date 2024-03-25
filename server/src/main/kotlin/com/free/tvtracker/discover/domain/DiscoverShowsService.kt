package com.free.tvtracker.discover.domain

import com.free.tvtracker.core.tmdb.TmdbClient
import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.data.TmdbTrendingResponse
import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import org.springframework.stereotype.Service

@Service
class DiscoverShowsService(
    private val logger: TvtrackerLogger,
    private val tmdbClient: TmdbClient,
) {
    fun getTrendingShows(): TmdbTrendingResponse {
        val respEntity = tmdbClient.get(
            "/3/trending/tv/day",
            TmdbTrendingResponse::class.java
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


