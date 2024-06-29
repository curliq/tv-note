package com.free.tvtracker.details.response

import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Cast
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Crew
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Images
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Video
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.WatchProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieDetailsApiModel(
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,
    val budget: Int?,
    val genres: List<String> = emptyList(),
    val homepage: String?,
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String?,
    @SerialName("origin_country")
    val originCountry: List<String?>?,
    @SerialName("original_language")
    val originalLanguage: String?,
    @SerialName("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("production_companies")
    val productionCompanies: List<TmdbShowDetailsApiModel.ProductionCompanies>?,
    @SerialName("production_countries")
    val productionCountries: List<TmdbShowDetailsApiModel.ProductionCountries>?,
    @SerialName("release_date")
    val releaseDate: String?,
    val revenue: Int?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("vote_count")
    val voteCount: Int?,
    @SerialName("videos")
    val videos: List<Video>? = null,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("cast")
    val cast: List<Cast>? = null,
    @SerialName("crew")
    val crew: List<Crew>? = null,
    @SerialName("watch_provider")
    val watchProvider: List<WatchProvider>? = null,
)

@Serializable
data class BelongsToCollection(
    val id: Int,
    val name: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
)
