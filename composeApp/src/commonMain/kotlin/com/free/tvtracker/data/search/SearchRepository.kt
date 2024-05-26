package com.free.tvtracker.data.search

import com.free.tvtracker.Endpoints
import com.free.tvtracker.base.ApiError
import com.free.tvtracker.core.data.http.TvHttpClient
import com.free.tvtracker.discover.request.RecommendedContentApiRequest
import com.free.tvtracker.discover.request.TmdbPersonRequestBody
import com.free.tvtracker.discover.request.TmdbShowDetailsRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowTrendingApiResponse
import com.free.tvtracker.search.request.MediaType
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiResponse
import io.ktor.utils.io.CancellationException

class SearchRepository(private val httpClient: TvHttpClient) {
    suspend fun searchAll(term: String): SearchApiResponse {
        val body = SearchApiRequestBody(term, MediaType.ALL)
        return httpClient.call(Endpoints.search, body)
    }

    suspend fun searchTvShows(term: String): SearchApiResponse {
        val body = SearchApiRequestBody(term, MediaType.TV_SHOWS)
        return try {
            httpClient.call(Endpoints.search, body)
        } catch (e: Throwable) {
            if (e is CancellationException) {
                SearchApiResponse.error(ApiError.Cancelled)
            } else {
                SearchApiResponse.error(ApiError.Network)
            }
        }
    }

    suspend fun getShow(showTmdbId: Int, includeEpisodes: Boolean): TmdbShowDetailsApiResponse {
        val body = TmdbShowDetailsRequestBody(showTmdbId, includeEpisodes)
        return try {
            httpClient.call(Endpoints.getTmdbShow, body)
        } catch (e: Throwable) {
            TmdbShowDetailsApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getPerson(tmdbPersonId: Int): TmdbPersonDetailsApiResponse {
        val body = TmdbPersonRequestBody(tmdbPersonId)
        return try {
            httpClient.call(Endpoints.getTmdbPerson, body)
        } catch (e: Throwable) {
            TmdbPersonDetailsApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getTrendingWeekly(): TmdbShowTrendingApiResponse {
        return try {
            httpClient.call(Endpoints.getTrendingWeekly)
        } catch (e: Throwable) {
            TmdbShowTrendingApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getNewEpisodeReleasedSoon(): TmdbShowTrendingApiResponse {
        return try {
            httpClient.call(Endpoints.getNewEpisodeReleasedSoon)
        } catch (e: Throwable) {
            TmdbShowTrendingApiResponse.error(ApiError.Network)
        }
    }

    suspend fun getRecommended(relatedShows: List<Int>): RecommendedContentApiResponse {
        return try {
            val body = RecommendedContentApiRequest(relatedShows)
            httpClient.call(Endpoints.getRecommendedContent, body)
        } catch (e: Throwable) {
            RecommendedContentApiResponse.error(ApiError.Network)
        }
    }
}