package com.free.tvtracker

import com.free.tvtracker.base.ApiResponse
import com.free.tvtracker.discover.request.RecommendedContentApiRequestBody
import com.free.tvtracker.discover.request.TmdbPersonApiRequestBody
import com.free.tvtracker.discover.request.TmdbShowDetailsApiRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowTrendingApiResponse
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.tracked.request.AddEpisodesApiRequestBody
import com.free.tvtracker.tracked.request.AddShowApiRequestBody
import com.free.tvtracker.tracked.request.SetShowWatchlistedApiRequestBody
import com.free.tvtracker.tracked.response.AddTrackedEpisodesApiResponse
import com.free.tvtracker.tracked.response.AddTrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowApiResponse
import com.free.tvtracker.tracked.response.TrackedShowsApiResponse
import com.free.tvtracker.user.request.LoginApiRequestBody
import com.free.tvtracker.user.request.PostFcmTokenApiRequestBody
import com.free.tvtracker.user.request.SignupApiRequestBody
import com.free.tvtracker.user.request.UpdatePreferencesApiRequestBody
import com.free.tvtracker.user.response.SessionApiResponse
import com.free.tvtracker.user.response.UserApiResponse
import kotlin.reflect.KClass

object Endpoints {
    object Path {
        const val GET_USER = ""
        const val POST_USER_CREDENTIALS = "user/complete-credentials"
        const val UPDATE_PREFERENCES = "user/update-push-notifications"
        const val CREATE_ANON_USER = "user/create-anon"
        const val LOGIN = "user/login"
        const val POST_FCM_TOKEN = "user/fcm-token"
        const val GET_WATCHING = "track/shows/watching"
        const val GET_FINISHED = "track/shows/finished"
        const val GET_WATCHLISTED = "track/shows/watchlisted"
        const val ADD_TRACKED = "track/shows"
        const val ADD_EPISODES = "track/episodes"
        const val SET_SHOW_WATCHLISTED = "track/shows/toggle-watchlist"
        const val SEARCH = "search"
        const val GET_TMDB_SHOW = "search/show"
        const val GET_TMDB_PERSON = "search/person"
        const val GET_TRENDING_WEEKLY = "discover/trending"
        const val GET_RELEASED_SOON = "discover/released-soon"
        const val GET_RECOMMENDED_CONTENT = "discover/recommended"
    }

    val getUser = EndpointNoBody(Path.GET_USER, UserApiResponse::class, Endpoint.Verb.GET)
    val createAnonUser = EndpointNoBody(Path.CREATE_ANON_USER, SessionApiResponse::class, Endpoint.Verb.POST)
    val postUserCredentials =
        Endpoint(Path.POST_USER_CREDENTIALS, SessionApiResponse::class, SignupApiRequestBody::class, Endpoint.Verb.POST)
    val login = Endpoint(Path.LOGIN, SessionApiResponse::class, LoginApiRequestBody::class, Endpoint.Verb.POST)
    val updateUserPreferences = Endpoint(
        Path.UPDATE_PREFERENCES,
        UserApiResponse::class,
        UpdatePreferencesApiRequestBody::class,
        Endpoint.Verb.POST
    )
    val postFcmToken = Endpoint(
        Path.POST_FCM_TOKEN,
        ApiResponse.EmptyApiResponse::class,
        PostFcmTokenApiRequestBody::class,
        Endpoint.Verb.POST
    )
    val getWatching = EndpointNoBody(Path.GET_WATCHING, TrackedShowsApiResponse::class, Endpoint.Verb.GET)
    val getFinished = EndpointNoBody(Path.GET_FINISHED, TrackedShowsApiResponse::class, Endpoint.Verb.GET)
    val getWatchlisted = EndpointNoBody(Path.GET_WATCHLISTED, TrackedShowsApiResponse::class, Endpoint.Verb.GET)
    val addTracked =
        Endpoint(Path.ADD_TRACKED, AddTrackedShowApiResponse::class, AddShowApiRequestBody::class, Endpoint.Verb.POST)
    val setShowWatchlisted = Endpoint(
        Path.SET_SHOW_WATCHLISTED,
        TrackedShowApiResponse::class,
        SetShowWatchlistedApiRequestBody::class,
        Endpoint.Verb.POST
    )
    val addEpisodes =
        Endpoint(
            Path.ADD_EPISODES,
            AddTrackedEpisodesApiResponse::class,
            AddEpisodesApiRequestBody::class,
            Endpoint.Verb.POST
        )

    //    val toggleWatchlist = Endpoint(Path.TOGGLE_WATCHLIST, )
    val search = Endpoint(Path.SEARCH, SearchApiResponse::class, SearchApiRequestBody::class, Endpoint.Verb.POST)
    val getTmdbShow = Endpoint(
        Path.GET_TMDB_SHOW,
        TmdbShowDetailsApiResponse::class,
        TmdbShowDetailsApiRequestBody::class,
        Endpoint.Verb.POST
    )
    val getTmdbPerson = Endpoint(
        Path.GET_TMDB_PERSON,
        TmdbPersonDetailsApiResponse::class,
        TmdbPersonApiRequestBody::class,
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
            RecommendedContentApiRequestBody::class,
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
