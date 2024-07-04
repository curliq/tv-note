package com.free.tvtracker.features.discover.domain

import com.free.tvtracker.Endpoints
import com.free.tvtracker.discover.request.RecommendedContentApiRequestBody
import com.free.tvtracker.discover.request.PagedContentApiRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiModel
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.discover.response.TmdbShowTrendingApiResponse
import com.free.tvtracker.discover.response.TrendingShowApiModel
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.search.response.SmallShowApiModel
import com.free.tvtracker.tmdb.data.TmdbShowSmallResponse
import com.free.tvtracker.tmdb.data.TmdbTrendingShowsResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = ["application/json"]
)
class DiscoverShowsController(
    val logger: TvtrackerLogger,
    val discoverShowsService: DiscoverShowsService,
) {

    @PostMapping(Endpoints.Path.GET_TRENDING_WEEKLY_SHOWS)
    fun trending(@RequestBody body: PagedContentApiRequestBody): ResponseEntity<TmdbShowTrendingApiResponse> {
        val res = discoverShowsService.getTrendingWeeklyShows(body.page)
        return ResponseEntity.ok(
            TmdbShowTrendingApiResponse.ok(
                res.toApiModel()
            )
        )
    }

    @PostMapping(Endpoints.Path.GET_RECOMMENDED_CONTENT_SHOWS)
    fun recommended(@RequestBody body: RecommendedContentApiRequestBody): ResponseEntity<RecommendedContentApiResponse>? {
        val res = discoverShowsService.getRecommended(body)
        return res
    }

    @PostMapping(Endpoints.Path.GET_RELEASED_SOON_SHOWS)
    fun releasedSoon(@RequestBody body: PagedContentApiRequestBody): ResponseEntity<TmdbShowTrendingApiResponse> {
        val res = discoverShowsService.getReleasedSoonShows(body.page)
        return ResponseEntity.ok(
            TmdbShowTrendingApiResponse.ok(
                res.toApiModel()
            )
        )
    }
}

fun TmdbTrendingShowsResponse.toApiModel(): TrendingShowApiModel {
    return TrendingShowApiModel(
        page = this.page ?: 0,
        totalPages = this.totalPages ?: 1,
        results = this.results.map {
            SmallShowApiModel(
                tmdbId = it.id!!,
                name = it.name!!,
                originalLanguage = it.originalLanguage,
                originalName = it.originalName,
                overview = it.overview,
                posterPath = it.posterPath,
                genreIds = it.genreIds,
                popularity = it.popularity,
                firstAirDate = it.firstAirDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                originCountry = it.originCountry,
            )
        }
    )
}

fun List<TmdbShowSmallResponse>.toApiModel(related: List<Int>): RecommendedContentApiModel {
    return RecommendedContentApiModel(
        results = this.map {
            RecommendedContentApiModel.Data(
                tmdbId = it.id!!,
                name = it.name!!,
                posterPath = it.posterPath,
                genreIds = it.genreIds,
                popularity = it.popularity,
                firstAirDate = it.firstAirDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
            )
        },
        relatedContent = related.map {
            RecommendedContentApiModel.RelatedContent(it)
        }
    )
}
