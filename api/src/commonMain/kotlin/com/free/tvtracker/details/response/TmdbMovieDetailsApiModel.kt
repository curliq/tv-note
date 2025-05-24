package com.free.tvtracker.details.response

import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Cast
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Crew
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Images
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.Video
import com.free.tvtracker.details.response.TmdbShowDetailsApiModel.WatchProvider
import com.free.tvtracker.search.response.SmallMovieApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieDetailsApiModel(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,
    val budget: Double? = null,
    val genres: List<String> = emptyList(),
    val homepage: String? = null,
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("origin_country")
    val originCountry: List<String?>? = null,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<TmdbShowDetailsApiModel.ProductionCompanies>? = null,
    @SerialName("production_countries")
    val productionCountries: List<TmdbShowDetailsApiModel.ProductionCountries>? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val revenue: Double? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null,
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
    val overview: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val movies: List<SmallMovieApiModel>
)
