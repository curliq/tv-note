package com.free.tvtracker.features.discover.domain

import com.free.tvtracker.Endpoints
import com.free.tvtracker.discover.request.PagedContentApiRequestBody
import com.free.tvtracker.discover.request.RecommendedContentApiRequestBody
import com.free.tvtracker.discover.response.RecommendedContentApiModel
import com.free.tvtracker.discover.response.RecommendedContentApiResponse
import com.free.tvtracker.discover.response.TmdbMovieTrendingApiResponse
import com.free.tvtracker.discover.response.TrendingMoviesApiModel
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.tmdb.data.TmdbMovieSmallResponse
import com.free.tvtracker.tmdb.data.TmdbTrendingMoviesResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = ["application/json"]
)
class DiscoverMoviesController(
    val logger: TvtrackerLogger,
    val discoverMoviesService: DiscoverMoviesService,
) {

    @PostMapping(Endpoints.Path.GET_TRENDING_WEEKLY_MOVIES)
    fun trendingMovies(@RequestBody body: PagedContentApiRequestBody): ResponseEntity<TmdbMovieTrendingApiResponse> {
        val res = discoverMoviesService.getTrendingWeeklyMovies(body.page)
        return ResponseEntity.ok(
            TmdbMovieTrendingApiResponse.ok(
                res.toApiModel()
            )
        )
    }

    @PostMapping(Endpoints.Path.GET_RECOMMENDED_CONTENT_MOVIES)
    fun recommended(@RequestBody body: RecommendedContentApiRequestBody): ResponseEntity<RecommendedContentApiResponse>? {
        val res = discoverMoviesService.getRecommendedMovies(body)
        return res
    }

    @PostMapping(Endpoints.Path.GET_RELEASED_SOON_MOVIES)
    fun releasedSoon(@RequestBody body: PagedContentApiRequestBody): ResponseEntity<TmdbMovieTrendingApiResponse> {
        val res = discoverMoviesService.getReleasedSoonMovies(body.page)
        return ResponseEntity.ok(
            TmdbMovieTrendingApiResponse.ok(
                res.toApiModel()
            )
        )
    }
}

fun TmdbTrendingMoviesResponse.toApiModel(): TrendingMoviesApiModel {
    return TrendingMoviesApiModel(
        page = this.page ?: 0,
        totalPages = this.totalPages ?: 1,
        results = this.results.map { it.toApiModel() }
    )
}

fun List<TmdbMovieSmallResponse>.toApiModel(related: List<Int>): RecommendedContentApiModel {
    return RecommendedContentApiModel(
        results = this.map {
            RecommendedContentApiModel.Data(
                tmdbId = it.id!!,
                name = it.title!!,
                posterPath = it.posterPath,
                genreIds = it.genreIds,
                popularity = it.popularity,
                firstAirDate = it.releaseDate,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
            )
        },
        relatedContent = related.map {
            RecommendedContentApiModel.RelatedContent(it)
        }
    )
}
