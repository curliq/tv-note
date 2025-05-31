package com.free.tvtracker.features.watchlists.api

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.features.watchlists.domain.WatchlistService
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.logging.error
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
import com.free.tvtracker.watchlists.requests.AddWatchlistApiRequestBody
import com.free.tvtracker.watchlists.requests.AddWatchlistContentApiRequestBody
import com.free.tvtracker.watchlists.requests.DeleteWatchlistApiRequestBody
import com.free.tvtracker.watchlists.requests.DeleteWatchlistContentApiRequestBody
import com.free.tvtracker.watchlists.requests.GetWatchlistContentApiRequestBody
import com.free.tvtracker.watchlists.requests.RenameWatchlistApiRequestBody
import com.free.tvtracker.watchlists.response.WatchlistsApiResponse
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
class WatchlistController(
    val logger: TvtrackerLogger,
    val watchlistService: WatchlistService,
) {

    @GetMapping(Endpoints.Path.GET_WATCHLISTS)
    fun getWatchlists(): ResponseEntity<WatchlistsApiResponse> {
        val res = try {
            watchlistService.getWatchlists()
        } catch (e: Exception) {
            logger.get.error(e)
            return ResponseEntity(
                WatchlistsApiResponse.error(ApiError.Network),
                HttpStatus.BAD_REQUEST
            )
        }
        return ResponseEntity.ok(WatchlistsApiResponse.ok(res))
    }

    @PostMapping(Endpoints.Path.ADD_WATCHLISTS)
    fun createWatchlist(@RequestBody body: AddWatchlistApiRequestBody): ResponseEntity<WatchlistsApiResponse> {
        val res = try {
            watchlistService.saveWatchlist(body.name)
        } catch (e: Exception) {
            logger.get.error(e)
            return ResponseEntity(
                WatchlistsApiResponse.error(ApiError.Network),
                HttpStatus.BAD_REQUEST
            )
        }
        return ResponseEntity.ok(WatchlistsApiResponse.ok(res))
    }

    @PostMapping(Endpoints.Path.GET_WATCHLIST_CONTENT)
    fun getWatchlistContent(
        @RequestBody body: GetWatchlistContentApiRequestBody
    ): ResponseEntity<TrackedShowsApiResponse> {
        val res = try {
            watchlistService.getWatchlistContent(body.watchlistId)
        } catch (e: Exception) {
            logger.get.error(e)
            return ResponseEntity(
                TrackedShowsApiResponse.error(ApiError.Network),
                HttpStatus.BAD_REQUEST
            )
        }
        return ResponseEntity.ok(TrackedShowsApiResponse.ok(res))
    }

    @PostMapping(Endpoints.Path.ADD_TRACKED_CONTENT_TO_WATCHLIST)
    fun addTrackedContentToWatchlist(
        @RequestBody body: AddWatchlistContentApiRequestBody
    ): ResponseEntity<TrackedShowApiResponse> {
        val content =
            watchlistService.addTrackedContentToWatchlist(body.trackedContentId, body.watchlistId, body.isTvShow)
        if (content == null) {
            return ResponseEntity(
                TrackedShowApiResponse.error(ApiError.Network),
                HttpStatus.BAD_REQUEST
            )
        }
        return ResponseEntity.ok(TrackedShowApiResponse.ok(content))
    }

    @PostMapping(Endpoints.Path.REMOVE_TRACKED_CONTENT_TO_WATCHLIST)
    fun deleteTrackedContentFromWatchlist(
        @RequestBody body: DeleteWatchlistContentApiRequestBody
    ): ResponseEntity<Unit> {
        try {
            watchlistService.deleteTrackedContentFromWatchlist(body.trackedContentId, body.watchlistId, body.isTvShow)
        } catch (e: Exception) {
            logger.get.error(e)
            return ResponseEntity(Unit, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(Unit)
    }

    @PostMapping(Endpoints.Path.DELETE_WATCHLISTS)
    fun deleteWatchlist(@RequestBody body: DeleteWatchlistApiRequestBody): ResponseEntity<Unit> {
        try {
            watchlistService.deleteWatchlist(body.watchlistId)
        } catch (e: Exception) {
            logger.get.error(e)
            return ResponseEntity(Unit, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(Unit)
    }

    @PostMapping(Endpoints.Path.EDIT_WATCHLIST)
    fun renameWatchlist(@RequestBody body: RenameWatchlistApiRequestBody): ResponseEntity<WatchlistsApiResponse> {
        try {
            val res = watchlistService.renameWatchlist(body.watchlistId, body.name)
            return ResponseEntity.ok(WatchlistsApiResponse.ok(res))
        } catch (e: Exception) {
            logger.get.error(e)
            return ResponseEntity(
                WatchlistsApiResponse.error(ApiError.Network),
                HttpStatus.BAD_REQUEST
            )
        }
    }
}
