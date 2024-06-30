package com.free.tvtracker.features.search.mappers

import com.free.tvtracker.base.MapperWithOptions
import com.free.tvtracker.details.response.BelongsToCollection
import com.free.tvtracker.details.response.TmdbMovieDetailsApiModel
import com.free.tvtracker.tmdb.data.TmdbMovieBigResponse
import com.free.tvtracker.tmdb.data.TmdbMovieCollectionResponse
import org.springframework.stereotype.Component

@Component
class MovieApiModelMapper :
    MapperWithOptions<TmdbMovieBigResponse, TmdbMovieDetailsApiModel, MovieApiModelMapper.Options> {

    data class Options(
        val countryCode: String,
        val collection: TmdbMovieCollectionResponse?
    )

    override fun map(from: TmdbMovieBigResponse, options: Options): TmdbMovieDetailsApiModel {
        return TmdbMovieDetailsApiModel(
            backdropPath = from.backdropPath,
            belongsToCollection = options.collection?.run {
                BelongsToCollection(
                    id = this.id,
                    name = this.name,
                    overview = this.overview,
                    posterPath = this.posterPath,
                    backdropPath = this.backdropPath,
                    movies = this.parts.sortedBy { it.releaseDate }.map { it.toApiModel() }
                )
            },
            budget = from.budget,
            genres = from.genres?.mapNotNull { it.name } ?: emptyList(),
            homepage = from.homepage,
            id = from.id,
            imdbId = from.imdbId,
            originCountry = from.originCountry,
            originalLanguage = from.originalLanguage,
            originalTitle = from.originalTitle,
            overview = from.overview,
            popularity = from.popularity,
            posterPath = from.posterPath,
            productionCompanies = from.productionCompanies.map { it.toApiModel() },
            productionCountries = from.productionCountries.map { it.toApiModel() },
            releaseDate = from.releaseDate,
            revenue = from.revenue,
            runtime = from.runtime,
            status = from.status,
            tagline = from.tagline,
            title = from.title,
            video = from.video,
            voteAverage = from.voteAverage,
            voteCount = from.voteCount,
            videos = from.videos?.results?.map { it.toApiModel() },
            images = from.images?.toApiModel(),
            cast = from.credits?.cast?.map { it.toApiModel() },
            crew = from.credits?.crew?.map { it.toApiModel() },
            watchProvider = from.watchProviders?.results?.get(options.countryCode.uppercase())?.flatrate?.map {
                it.toApiModel()
            }
        )
    }
}
