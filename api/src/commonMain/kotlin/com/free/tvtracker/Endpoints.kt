package com.free.tvtracker

import com.free.tvtracker.base.ApiError
import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.discover.request.RecommendedContentApiRequest
import com.free.tvtracker.discover.request.TmdbPersonRequestBody
import com.free.tvtracker.discover.request.TmdbShowDetailsRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowTrendingApiResponse
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.tracked.request.AddEpisodesRequest
import com.free.tvtracker.tracked.request.AddShowRequest
import com.free.tvtracker.tracked.response.AddTrackedEpisodesApiResponse
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import com.free.tvtracker.user.request.PostFcmTokenRequest
import com.free.tvtracker.user.response.UserApiResponse
import kotlin.reflect.KClass

object Endpoints {
    object Path {
        const val GET_USER = ""
        const val POST_USER_CREDENTIALS = "user/complete-credentials"
        const val CREATE_ANON_USER = "user/create-anon"
        const val LOGIN = "user/login"
        const val POST_FCM_TOKEN = "user/fcm-token"
        const val GET_WATCHING = "track/shows/watching"
        const val GET_FINISHED = "track/shows/finished"
        const val GET_WATCHLISTED = "track/shows/watchlisted"
        const val ADD_TRACKED = "track/shows"
        const val ADD_EPISODES = "track/episodes"
        const val TOGGLE_WATCHLIST = "track/shows/toggle-watchlist"
        const val SEARCH = "search"
        const val GET_TMDB_SHOW = "search/show"
        const val GET_TMDB_PERSON = "search/person"
        const val GET_TRENDING_WEEKLY = "discover/trending"
        const val GET_RELEASED_SOON = "discover/released-soon"
        const val GET_RECOMMENDED_CONTENT = "discover/recommended"
    }

    val getUser = EndpointNoBody(Path.GET_USER, UserApiResponse::class, Endpoint.Verb.GET)
    val postFcmToken = Endpoint(
        Path.POST_FCM_TOKEN,
        ApiResponse.EmptyApiResponse::class,
        PostFcmTokenRequest::class,
        Endpoint.Verb.POST
    )
    val getWatching = EndpointNoBody(Path.GET_WATCHING, TrackedShowApiResponse::class, Endpoint.Verb.GET)
    val getFinished = EndpointNoBody(Path.GET_FINISHED, TrackedShowApiResponse::class, Endpoint.Verb.GET)
    val getWatchlisted = EndpointNoBody(Path.GET_WATCHLISTED, TrackedShowApiResponse::class, Endpoint.Verb.GET)
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
    val getTmdbPerson = Endpoint(
        Path.GET_TMDB_PERSON,
        TmdbPersonDetailsApiResponse::class,
        TmdbPersonRequestBody::class,
        Endpoint.Verb.POST
    )
    val getTrendingWeekly =
        EndpointNoBody(Path.GET_TRENDING_WEEKLY, TmdbShowTrendingApiResponse::class, Endpoint.Verb.GET)
    val getNewEpisodeReleasedSoon =
        EndpointNoBody(Path.GET_RELEASED_SOON, TmdbShowTrendingApiResponse::class, Endpoint.Verb.GET)
    val getRecommendedContent =
        Endpoint(
            Path.GET_RECOMMENDED_CONTENT,
            RecommendedContentApiResponse::class,
            RecommendedContentApiRequest::class,
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
