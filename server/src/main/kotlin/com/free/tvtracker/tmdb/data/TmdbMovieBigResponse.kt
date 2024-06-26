package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbMovieBigResponse(
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
    @JsonProperty("belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,
    val budget: Int?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Int,
    @JsonProperty("imdb_id")
    val imdbId: String?,
    @JsonProperty("origin_country")
    val originCountry: List<String?>?,
    @JsonProperty("original_language")
    val originalLanguage: String?,
    @JsonProperty("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    @JsonProperty("production_companies")
    val productionCompanies: List<ProductionCompany>?,
    @JsonProperty("production_countries")
    val productionCountries: List<ProductionCountry>?,
    @JsonProperty("release_date")
    val releaseDate: String?,
    val revenue: Int?,
    val runtime: Int?,
    @JsonProperty("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    @JsonProperty("vote_average")
    val voteAverage: Double?,
    @JsonProperty("vote_count")
    val voteCount: Int?,
)

data class BelongsToCollection(
    val id: Int,
    val name: String?,
    @JsonProperty("poster_path")
    val posterPath: String?,
    @JsonProperty("backdrop_path")
    val backdropPath: String?,
)

data class Genre(
    val id: Int,
    val name: String?,
)

data class ProductionCompany(
    val id: Int,
    @JsonProperty("logo_path")
    val logoPath: String?,
    val name: String?,
    @JsonProperty("origin_country")
    val originCountry: String?,
)

data class ProductionCountry(
    @JsonProperty("iso_3166_1")
    val iso31661: String?,
    val name: String?,
)

data class SpokenLanguage(
    @JsonProperty("english_name")
    val englishName: String?,
    @JsonProperty("iso_639_1")
    val iso6391: String?,
    val name: String?,
)
