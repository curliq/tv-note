package com.free.tvtracker

import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.discover.request.TmdbShowDetailsRequestBody
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.tracked.response.AddTrackedEpisodesApiResponse
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import com.free.tvtracker.user.response.UserApiResponse
import kotlin.reflect.KClass

object Endpoints {
    object Path {
        const val GET_USER: String = "track/shows/watching"
        const val WATCHING: String = "track/shows/watching"
        const val FINISHED: String = "track/shows/finished"
        const val WATCHLISTED: String = "track/shows/watchlisted"
        const val ADD_TRACKED: String = "track/shows"
        const val ADD_EPISODES: String = "track/episodes"
        const val TOGGLE_WATCHLIST: String = "track/shows/toggle-watchlist"
        const val SEARCH: String = "search"
        const val GET_TMDB_SHOW: String = "search/show"
    }

    val getUser = EndpointNoBody(Path.GET_USER, UserApiResponse::class, Endpoint.Verb.GET)
    val getWatching = EndpointNoBody(Path.WATCHING, TrackedShowApiResponse::class, Endpoint.Verb.GET)
    val getFinished = EndpointNoBody(Path.FINISHED, TrackedShowApiResponse::class, Endpoint.Verb.GET)
    val getWatchlisted = EndpointNoBody(Path.WATCHLISTED, TrackedShowApiResponse::class, Endpoint.Verb.GET)
    val addTracked =
        Endpoint(Path.ADD_TRACKED, AddTrackedShowApiResponse::class, AddShowRequest::class, Endpoint.Verb.POST)
    val addEpisodes =
        Endpoint(Path.ADD_EPISODES, AddTrackedEpisodesApiResponse::class, AddEpisodesRequest::class, Endpoint.Verb.POST)
//    val toggleWatchlist = Endpoint(Path.TOGGLE_WATCHLIST, )
    val search = Endpoint(Path.SEARCH, SearchApiResponse::class, SearchApiRequestBody::class, Endpoint.Verb.POST)
    val getTmdbShow = Endpoint(
        Path.GET_TMDB_SHOW,
        TmdbShowDetailsApiResponse::class,
        TmdbShowDetailsRequestBody::class,
        Endpoint.Verb.POST
    )
}

open class Endpoint<ReturnType : ApiResponse<out Any>, BodyType : Any>(
    open val path: String,
    open val returnType: KClass<ReturnType>,
    val requestBodyType: KClass<BodyType>,
    open val verb: Verb,
) {
    enum class Verb { GET, POST }
}

data class EndpointNoBody<ReturnType : ApiResponse<out Any>>(
    override val path: String,
    override val returnType: KClass<ReturnType>,
    override val verb: Verb,
) : Endpoint<ReturnType, Nothing>(path, returnType, Nothing::class, verb)
