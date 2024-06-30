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
    val productionCompanies: List<TmdbShowBigResponse.ProductionCompanies> = emptyList(),
    @JsonProperty("production_countries")
    val productionCountries: List<TmdbShowBigResponse.ProductionCountries> = emptyList(),
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
    @JsonProperty("watch/providers")
    val watchProviders: TmdbShowWatchProvidersResponse?,
    @JsonProperty("credits")
    val credits: TmdbShowAggregatedCreditsResponse?,
    @JsonProperty("videos")
    val videos: TmdbShowVideosResponse?,
    @JsonProperty("images")
    val images: TmdbShowImagesResponse?
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

data class SpokenLanguage(
    @JsonProperty("english_name")
    val englishName: String?,
    @JsonProperty("iso_639_1")
    val iso6391: String?,
    val name: String?,
)
