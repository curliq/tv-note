package com.free.tvtracker.tracked.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.besttvtracker.TrackedShowApiModel
import com.free.tvtracker.core.data.ApiResponse
import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.tracked.domain.WatchedShowsService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(
    "/track/shows",
    produces = ["application/json"]
)
class WatchedShowsController(
    val logger: TvtrackerLogger,
    val watchedShowsService: WatchedShowsService
) {

    data class AddShowRequest(
        @JsonProperty("tmdb_id") val tmdbShowId: Int,
        @JsonProperty("wishlisted") val wishlisted: Boolean
    )

    @PostMapping("")
    fun addShow(@RequestBody body: AddShowRequest): ApiResponse<TrackedShowApiModel> {
        val ERROR_SHOW_ALREADY_ADDED = 1
        val ERROR_SHOW_IS_GONE = 2
        val res = try {
            watchedShowsService.addShow(body)
        } catch (e: org.springframework.dao.DataIntegrityViolationException) {
            e.printStackTrace()
            return ApiResponse.error(ERROR_SHOW_ALREADY_ADDED)
        }
        if (res == null) {
            return ApiResponse.error(ERROR_SHOW_IS_GONE)
        }
        return ApiResponse.ok(res.toApiModel())
    }

    data class AddEpisodeRequest(
        @JsonProperty("episode_ids") val episodeIds: List<String>
    )

    @PostMapping("/{watchedShowId}/episode")
    fun episodeWatched(@PathVariable watchedShowId: Int, @RequestBody body: AddEpisodeRequest): ApiResponse<Boolean> {
        watchedShowsService.addEpisode(watchedShowId, body)
        return ApiResponse.ok(true)
    }

    @GetMapping("/watching")
    fun getOngoing(): ApiResponse<List<TrackedShowApiModel>> {
        val shows = watchedShowsService.getOngoingShows().map { it.toApiModel()}
        return ApiResponse.ok(shows)
    }

    @GetMapping("/watchlist")
    fun getWatchlist(): ApiResponse<List<TrackedShowApiModel>> {
        return ApiResponse.ok(watchedShowsService.getWatchlistedShows().map { it.toApiModel()})
    }

    @GetMapping("/finished")
    fun getFinished(): ApiResponse<List<TrackedShowApiModel>> {
        return ApiResponse.ok(watchedShowsService.getFinishedShows().map { it.toApiModel()})
    }
}
