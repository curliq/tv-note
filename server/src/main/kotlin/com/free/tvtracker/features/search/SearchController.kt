package com.free.tvtracker.features.search

import com.free.tvtracker.Endpoints
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.tmdb.data.TmdbSearchMultiResponse
import com.free.tvtracker.tmdb.data.enums.TmdbContentType
import com.free.tvtracker.discover.request.TmdbPersonApiRequestBody
import com.free.tvtracker.discover.request.TmdbShowDetailsApiRequestBody
import com.free.tvtracker.discover.response.TmdbPersonDetailsApiResponse
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.search.request.MediaType
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiModel
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.search.response.SearchMovieApiModel
import com.free.tvtracker.search.response.SearchPersonApiModel
import com.free.tvtracker.search.response.SearchShowApiModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = ["application/json"]
)
class SearchController(
    val logger: TvtrackerLogger,
    val searchService: SearchService,
) {

    @PostMapping(Endpoints.Path.SEARCH)
    fun search(@RequestBody body: SearchApiRequestBody): ResponseEntity<SearchApiResponse> {
        val apiModel = searchService.searchTerm(body.term, body.mediaType).results.map { content ->
            when (body.mediaType) {
                MediaType.ALL -> {
                    when (TmdbContentType.entries.find { it.field == content.mediaType!! }) {
                        TmdbContentType.SHOW -> content.toShowApiModel()
                        TmdbContentType.MOVIE -> content.toMovieApiModel()
                        TmdbContentType.PERSON -> content.toPersonApiModel()
                        null -> null
                    }
                }

                MediaType.TV_SHOWS -> content.toShowApiModel()
                MediaType.MOVIES -> content.toMovieApiModel()
                MediaType.PEOPLE -> content.toPersonApiModel()
            }
        }
        val response = SearchApiResponse.ok(
            SearchApiModel(
                shows = apiModel.filterIsInstance<SearchShowApiModel>(),
                movies = apiModel.filterIsInstance<SearchMovieApiModel>(),
                persons = apiModel.filterIsInstance<SearchPersonApiModel>()
            )
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping(Endpoints.Path.GET_TMDB_SHOW)
    fun getTmdbShow(@RequestBody body: TmdbShowDetailsApiRequestBody): ResponseEntity<TmdbShowDetailsApiResponse> {
        return ResponseEntity.ok(
            TmdbShowDetailsApiResponse.ok(
                searchService.getShowApiModel(tmdbShowId = body.tmdbId, alwaysIncludeEpisodes = body.includeEpisodes)
            )
        )
    }

    @PostMapping(Endpoints.Path.GET_TMDB_PERSON)
    fun getTmdbPerson(@RequestBody body: TmdbPersonApiRequestBody): ResponseEntity<TmdbPersonDetailsApiResponse> {
        return ResponseEntity.ok(
            TmdbPersonDetailsApiResponse.ok(
                searchService.getPersonApiModel(tmdbPersonId = body.tmdbId)
            )
        )
    }
}

fun TmdbSearchMultiResponse.Data.toMovieApiModel(): SearchMovieApiModel {
    return SearchMovieApiModel(
        backdropPath = backdropPath,
        id = id!!,
        title = title!!,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )
}

fun TmdbSearchMultiResponse.Data.toShowApiModel(): SearchShowApiModel {
    return SearchShowApiModel(
        tmdbId = this.id!!,
        name = this.name!!,
        originalLanguage = this.originalLanguage,
        originalName = this.originalName,
        overview = this.overview,
        posterPath = this.posterPath,
        genreIds = this.genreIds,
        popularity = this.popularity,
        firstAirDate = this.firstAirDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        originCountry = this.originCountry,
    )
}

fun TmdbSearchMultiResponse.Data.toPersonApiModel(): SearchPersonApiModel {
    return SearchPersonApiModel(
        id = id!!,
        name = name!!,
        popularity = popularity,
        originCountry = originCountry,
        gender = gender,
        knownForDepartment = knownForDepartment,
    )
}
