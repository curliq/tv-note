package com.free.tvtracker.features.search

import com.free.tvtracker.Endpoints
import com.free.tvtracker.constants.TmdbContentType
import com.free.tvtracker.details.request.TmdbMovieDetailsApiRequestBody
import com.free.tvtracker.logging.TvtrackerLogger
import com.free.tvtracker.tmdb.data.TmdbSearchMultiResponse
import com.free.tvtracker.details.request.TmdbPersonApiRequestBody
import com.free.tvtracker.details.request.TmdbShowDetailsApiRequestBody
import com.free.tvtracker.details.response.TmdbMovieDetailsApiResponse
import com.free.tvtracker.details.response.TmdbPersonDetailsApiResponse
import com.free.tvtracker.details.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.search.request.MediaType
import com.free.tvtracker.search.request.SearchApiRequestBody
import com.free.tvtracker.search.response.SearchApiModel
import com.free.tvtracker.search.response.SearchApiResponse
import com.free.tvtracker.search.response.SmallMovieApiModel
import com.free.tvtracker.search.response.SearchMultiApiModel
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
            content.toMultiApiModel(body.mediaType)
        }
        val response = SearchApiResponse.ok(SearchApiModel(apiModel))
        return ResponseEntity.ok(response)
    }

    @PostMapping(Endpoints.Path.GET_TMDB_SHOW)
    fun getTmdbShow(@RequestBody body: TmdbShowDetailsApiRequestBody): ResponseEntity<TmdbShowDetailsApiResponse> {
        return ResponseEntity.ok(
            TmdbShowDetailsApiResponse.ok(
                searchService.getShowApiModel(body.tmdbId, body.includeEpisodes, body.countryCode)
            )
        )
    }

    @PostMapping(Endpoints.Path.GET_TMDB_MOVIE)
    fun getTmdbMovie(@RequestBody body: TmdbMovieDetailsApiRequestBody): ResponseEntity<TmdbMovieDetailsApiResponse> {
        return ResponseEntity.ok(
            TmdbMovieDetailsApiResponse.ok(
                searchService.getMovieApiModel(body.tmdbId, body.countryCode)
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

fun TmdbSearchMultiResponse.Data.toMovieApiModel(): SmallMovieApiModel {
    return SmallMovieApiModel(
        backdropPath = backdropPath,
        tmdbId = id!!,
        title = title!!,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
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
        tmdbId = id!!,
        name = name!!,
        popularity = popularity,
        originCountry = originCountry,
        gender = gender,
        knownForDepartment = knownForDepartment,
    )
}

/**
 * @param forcedMediaType this is used for when doing search/multi because the tmdb api excludes the media_type field
 * in this case
 */
fun TmdbSearchMultiResponse.Data.toMultiApiModel(forcedMediaType: MediaType): SearchMultiApiModel {
    val media = when (forcedMediaType) {
        MediaType.ALL -> "multi"
        MediaType.TV_SHOWS -> TmdbContentType.SHOW.key
        MediaType.MOVIES -> TmdbContentType.MOVIE.key
        MediaType.PEOPLE -> TmdbContentType.PERSON.key
    }
    return SearchMultiApiModel(
        tmdbId = id!!,
        adult = adult,
        backdropPath = backdropPath,
        title = title,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        mediaType = mediaType ?: media,
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        name = name,
        originalName = originalName,
        firstAirDate = firstAirDate,
        profilePath = profilePath,
        originCountry = originCountry,
        gender = gender,
        knownForDepartment = knownForDepartment,
    )
}
