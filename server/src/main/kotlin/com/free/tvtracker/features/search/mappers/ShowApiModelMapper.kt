package com.free.tvtracker.features.search.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.constants.SEASON_SPECIAL_NUMBER
import com.free.tvtracker.tmdb.data.TmdbShowBigResponse
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel
import org.springframework.stereotype.Component

@Component
class ShowApiModelMapper :
    MapperWithOptions<TmdbShowBigResponse, TmdbShowDetailsApiModel, ShowApiModelMapper.ShowApiModelMapperOptions> {

    data class ShowApiModelMapperOptions(
        val episodes: List<TmdbShowDetailsApiModel.Season.Episode>,
        val countryCode: String
    )

    override fun map(
        from: TmdbShowBigResponse,
        options: ShowApiModelMapperOptions
    ): TmdbShowDetailsApiModel {
        return TmdbShowDetailsApiModel(
            id = from.id,
            name = from.name!!,
            status = from.status!!,
            backdropPath = from.backdropPath,
            episodeRunTime = from.episodeRunTime,
            firstAirDate = from.firstAirDate,
            genres = from.genres.mapNotNull { it.name },
            homepage = from.homepage,
            inProduction = from.inProduction,
            languages = from.languages,
            lastAirDate = from.lastAirDate,
            lastEpisodeToAir = from.lastEpisodeToAir?.toApiModel(),
            nextEpisodeToAir = from.nextEpisodeToAir?.toApiModel(),
            networks = from.networks.map { it.toApiModel() },
            numberOfEpisodes = from.numberOfEpisodes,
            numberOfSeasons = from.numberOfSeasons,
            originCountry = from.originCountry,
            originalLanguage = from.originalLanguage,
            originalName = from.originalName,
            overview = from.overview,
            popularity = from.popularity,
            posterPath = from.posterPath,
            productionCompanies = from.productionCompanies.map { it.toApiModel() },
            productionCountries = from.productionCountries.map { it.toApiModel() },
            seasons = from.seasons.filter { it.seasonNumber != SEASON_SPECIAL_NUMBER }
                .map { it.toApiModel(options.episodes.filter { ep -> ep.seasonNumber == it.seasonNumber }) },
            tagline = from.tagline,
            type = from.type,
            voteAverage = from.voteAverage,
            voteCount = from.voteCount,
            videos = from.videos?.results?.map { it.toApiModel() },
            images = from.images?.toApiModel(),
            cast = from.credits?.cast?.map { it.toApiModel() },
            crew = from.credits?.crew?.map { it.toApiModel() },
            watchProvider = from.watchProviders?.results?.get(options.countryCode.uppercase())?.flatrate?.map {
                it.toApiModel()
            },
            imdbId = from.externalIds?.imdbId
        )
    }
}
