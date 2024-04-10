package com.free.tvtracker.tracked.api

import com.free.tvtracker.Endpoints
import com.free.tvtracker.core.data.ApiResponse
import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.tracked.domain.WatchedShowsService
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.tracked.response.AddTrackedEpisodesApiResponse
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.ErrorShowAlreadyAdded
import com.free.tvtracker.tracked.response.ErrorShowIsGone
import com.free.tvtracker.tracked.response.TrackedShowApiModel
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(
    produces = ["application/json"]
)
class TrackedShowsController(
    val logger: TvtrackerLogger,
    val watchedShowsService: WatchedShowsService
) {

    @PostMapping(Endpoints.Path.ADD_TRACKED)
    fun addShow(@RequestBody body: AddShowRequest): ResponseEntity<AddTrackedShowApiResponse> {
        val res = try {
            watchedShowsService.addShow(body)
        } catch (e: org.springframework.dao.DataIntegrityViolationException) {
            e.printStackTrace()
            return ResponseEntity(
                AddTrackedShowApiResponse.error(ErrorShowAlreadyAdded),
                HttpStatus.BAD_REQUEST
            )
        }
        if (res == null) {
            return ResponseEntity(
                AddTrackedShowApiResponse.error(ErrorShowIsGone),
                HttpStatus.BAD_REQUEST
            )
        }
        return ResponseEntity.ok(AddTrackedShowApiResponse.ok(res.toApiModel()))
    }

    @PostMapping(Endpoints.Path.ADD_EPISODES)
    fun episodeWatched(@RequestBody body: AddEpisodesRequest): ResponseEntity<AddTrackedEpisodesApiResponse> {
        val show = watchedShowsService.addEpisode(body)
        return ResponseEntity.ok(AddTrackedEpisodesApiResponse.ok(show.map { it.toApiModel() }))
    }

    @GetMapping(Endpoints.Path.WATCHING)
    fun getOngoing(): ResponseEntity<TrackedShowApiResponse> {
        val shows = watchedShowsService.getOngoingShows().map { it.toApiModel() }
        return ResponseEntity.ok(TrackedShowApiResponse.ok(shows))
    }

    @GetMapping("/watchlist")
    fun getWatchlist(): ApiResponse<List<TrackedShowApiModel>> {
        return ApiResponse.ok(watchedShowsService.getWatchlistedShows().map { it.toApiModel() })
    }

    @GetMapping("/finished")
    fun getFinished(): ApiResponse<List<TrackedShowApiModel>> {
        return ApiResponse.ok(watchedShowsService.getFinishedShows().map { it.toApiModel() })
    }
}
