package com.free.tvtracker.data.search

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.data.common.AuthenticatedRepository
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.expect.data.TvHttpClient
import com.free.tvtracker.discover.request.RecommendedContentApiRequestBody
import com.free.tvtracker.discover.request.TmdbPersonApiRequestBody
import com.free.tvtracker.discover.request.TmdbShowDetailsApiRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowTrendingApiResponse
import com.free.tvtracker.search.request.MediaType
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiResponse
import io.ktor.utils.io.CancellationException

class SearchRepository(
    httpClient: TvHttpClient,
    sessionRepository: SessionRepository
) : AuthenticatedRepository(httpClient, sessionRepository) {

    suspend fun searchAll(term: String): SearchApiResponse {
        val body = SearchApiRequestBody(term, MediaType.ALL)
        return httpClient.call(Endpoints.search, body)
    }

    suspend fun searchTvShows(term: String): SearchApiResponse {
        return try {
            val body = SearchApiRequestBody(term, MediaType.TV_SHOWS)
            super.call(Endpoints.search, body)
        } catch (e: Throwable) {
            if (e is CancellationException) {
                SearchApiResponse.error(ApiError.Cancelled)
            } else {
                SearchApiResponse.error(ApiError.Network)
            }
        }
    }

    suspend fun getShow(showTmdbId: Int, includeEpisodes: Boolean): TmdbShowDetailsApiResponse {
        val body = TmdbShowDetailsApiRequestBody(showTmdbId, includeEpisodes)
        return try {
            super.call(Endpoints.getTmdbShow, body)
        } catch (e: Throwable) {
            TmdbShowDetailsApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getPerson(tmdbPersonId: Int): TmdbPersonDetailsApiResponse {
        val body = TmdbPersonApiRequestBody(tmdbPersonId)
        return try {
            super.call(Endpoints.getTmdbPerson, body)
        } catch (e: Throwable) {
            TmdbPersonDetailsApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getTrendingWeekly(): TmdbShowTrendingApiResponse {
        return try {
            super.callNoBody(Endpoints.getTrendingWeekly)
        } catch (e: Throwable) {
            TmdbShowTrendingApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getNewEpisodeReleasedSoon(): TmdbShowTrendingApiResponse {
        return try {
            super.callNoBody(Endpoints.getNewEpisodeReleasedSoon)
        } catch (e: Throwable) {
            TmdbShowTrendingApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getRecommended(relatedShows: List<Int>): RecommendedContentApiResponse {
        return try {
            val body = RecommendedContentApiRequestBody(relatedShows)
            super.call(Endpoints.getRecommendedContent, body)
        } catch (e: Throwable) {
            RecommendedContentApiResponse.error(ApiError.Network)
        }
    }
}
