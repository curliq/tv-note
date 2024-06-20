package com.free.tvtracker.features.tracked.api

import com.free.tvtracker.Endpoints
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.features.tracked.domain.TrackedShowsService
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.request.SetShowWatchlistedApiRequestBody
import com.free.tvtracker.tracked.response.AddTrackedEpisodesApiResponse
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.ErrorShowAlreadyAdded
import com.free.tvtracker.tracked.response.ErrorShowIsGone
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
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
    val trackedShowsService: TrackedShowsService
) {

    @PostMapping(Endpoints.Path.ADD_TRACKED)
    fun addShow(@RequestBody body: AddShowApiRequestBody): ResponseEntity<AddTrackedShowApiResponse> {
        val res = try {
            trackedShowsService.addShow(body)
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
    fun episodeWatched(@RequestBody body: AddEpisodesApiRequestBody): ResponseEntity<AddTrackedEpisodesApiResponse> {
        val show = trackedShowsService.addEpisode(body)
        return ResponseEntity.ok(AddTrackedEpisodesApiResponse.ok(show.map { it.toApiModel() }))
    }

    @GetMapping(Endpoints.Path.GET_WATCHING)
    fun getOngoing(): ResponseEntity<TrackedShowsApiResponse> {
        val shows = trackedShowsService.getOngoingShows()
        return ResponseEntity.ok(TrackedShowsApiResponse.ok(shows))
    }

    @GetMapping(Endpoints.Path.GET_FINISHED)
    fun getFinished(): ResponseEntity<TrackedShowsApiResponse> {
        val shows = trackedShowsService.getFinishedShows()
        return ResponseEntity.ok(TrackedShowsApiResponse.ok(shows))
    }

    @GetMapping(Endpoints.Path.GET_WATCHLISTED)
    fun getWatchlist(): ResponseEntity<TrackedShowsApiResponse> {
        val shows = trackedShowsService.getWatchlistedShows()
        return ResponseEntity.ok(TrackedShowsApiResponse.ok(shows))
    }

    @PostMapping(Endpoints.Path.SET_SHOW_WATCHLISTED)
    fun setShowWatchlisted(
        @RequestBody body: SetShowWatchlistedApiRequestBody
    ): ResponseEntity<TrackedShowApiResponse> {
        val shows = trackedShowsService.setShowWatchlistFlag(body.trackedShowId, body.watchlisted)
        return ResponseEntity.ok(TrackedShowApiResponse.ok(shows))
    }
}
