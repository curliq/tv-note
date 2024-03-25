package com.free.tvtracker.discover.domain

import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.data.TmdbTrendingResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    "/discover",
    produces = ["application/json"]
)
class DiscoverShowsController(
    val logger: TvtrackerLogger,
    val discoverShowsService: DiscoverShowsService,
) {

    @GetMapping("/trending")
    fun trending(): TmdbTrendingResponse {


        logger.get.debug("@@@@@@@@@@@@@@@@@")

        return discoverShowsService.getTrendingShows()
    }

    @GetMapping("/recommended")
    fun recommended(): TmdbTrendingResponse? {
        return null
    }

    @GetMapping("/popular")
    fun popular(): TmdbTrendingResponse? {
        return null
    }

    @GetMapping("/coming_soon")
    fun comingSoon(): TmdbTrendingResponse? {
        return null
    }
}
