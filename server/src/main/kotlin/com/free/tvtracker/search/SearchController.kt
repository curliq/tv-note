package com.free.tvtracker.search

import com.free.tvtracker.Endpoints
import com.free.tvtracker.core.logging.TvtrackerLogger
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse
import com.free.tvtracker.core.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.core.tmdb.data.enums.TmdbContentType
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel
import com.free.tvtracker.discover.response.TmdbShowDetailsApiResponse
import com.free.tvtracker.discover.request.TmdbShowDetailsRequestBody
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
    fun getTmdbShow(@RequestBody body: TmdbShowDetailsRequestBody): ResponseEntity<TmdbShowDetailsApiResponse> {
        return ResponseEntity.ok(
            TmdbShowDetailsApiResponse.ok(
                searchService.getShow(tmdbShowId = body.tmdbId).toApiModel()
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


fun TmdbShowBigResponse.toApiModel() = TmdbShowDetailsApiModel(
    id = this.id!!,
    name = this.name!!,
    status = this.status!!,
    backdropPath = this.backdropPath,
    episodeRunTime = this.episodeRunTime,
    firstAirDate = this.firstAirDate,
    homepage = this.homepage,
    inProduction = this.inProduction,
    languages = this.languages,
    lastAirDate = this.lastAirDate,
    lastEpisodeToAir = this.lastEpisodeToAir?.toApiModel(),
    nextEpisodeToAir = this.nextEpisodeToAir?.toApiModel(),
    networks = this.networks.map { it.toApiModel() },
    numberOfEpisodes = this.numberOfEpisodes,
    numberOfSeasons = this.numberOfSeasons,
    originCountry = this.originCountry,
    originalLanguage = this.originalLanguage,
    originalName = this.originalName,
    overview = this.overview,
    popularity = this.popularity,
    posterPath = this.posterPath,
    productionCompanies = this.productionCompanies.map { it.toApiModel() },
    productionCountries = this.productionCountries.map { it.toApiModel() },
    seasons = this.seasons.map { it.toApiModel() },
    tagline = this.tagline,
    type = this.type,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
)

fun TmdbShowBigResponse.Episode.toApiModel() = TmdbShowDetailsApiModel.Episode(
    id = this.id,
    name = this.name,
    overview = this.overview,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
    airDate = this.airDate,
    episodeNumber = this.episodeNumber,
    episodeType = this.episodeType,
    productionCode = this.productionCode,
    runtime = this.runtime,
    seasonNumber = this.seasonNumber,
    showId = this.showId,
    stillPath = this.stillPath,
)

fun TmdbShowBigResponse.Networks.toApiModel() = TmdbShowDetailsApiModel.Networks(
    id = id!!,
    name = name!!,
    logoPath = logoPath,
    originCountry = originCountry,
)

fun TmdbShowBigResponse.ProductionCompanies.toApiModel() = TmdbShowDetailsApiModel.ProductionCompanies(
    id = this.id,
    logoPath = this.logoPath,
    name = this.name,
    originCountry = this.originCountry,
)

fun TmdbShowBigResponse.ProductionCountries.toApiModel() = TmdbShowDetailsApiModel.ProductionCountries(
    iso31661 = this.iso31661,
    name = this.name,
)

fun TmdbShowBigResponse.Seasons.toApiModel() = TmdbShowDetailsApiModel.Seasons(
    airDate = this.airDate,
    episodeCount = this.episodeCount,
    id = this.id,
    name = this.name,
    overview = this.overview,
    posterPath = this.posterPath,
    seasonNumber = this.seasonNumber,
    voteAverage = this.voteAverage,
)
