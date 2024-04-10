package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

data class TmdbShowBigResponse(
    @JsonProperty("adult") var adult: Boolean? = null,
    @JsonProperty("backdrop_path") var backdropPath: String? = null,
    @JsonProperty("created_by") var createdBy: ArrayList<CreatedBy> = arrayListOf(),
    @JsonProperty("episode_run_time") var episodeRunTime: ArrayList<Int> = arrayListOf(),
    @JsonProperty("first_air_date") var firstAirDate: String? = null,
    @JsonProperty("genres") var genres: ArrayList<Genres> = arrayListOf(),
    @JsonProperty("homepage") var homepage: String? = null,
    @JsonProperty("id") var id: Int? = null,
    @JsonProperty("in_production") var inProduction: Boolean? = null,
    @JsonProperty("languages") var languages: ArrayList<String> = arrayListOf(),
    @JsonProperty("last_air_date") var lastAirDate: String? = null,
    @JsonProperty("last_episode_to_air") var lastEpisodeToAir: Episode? = Episode(),
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("next_episode_to_air") var nextEpisodeToAir: Episode? = Episode(),
    @JsonProperty("networks") var networks: ArrayList<Networks> = arrayListOf(),
    @JsonProperty("number_of_episodes") var numberOfEpisodes: Int? = null,
    @JsonProperty("number_of_seasons") var numberOfSeasons: Int? = null,
    @JsonProperty("origin_country") var originCountry: ArrayList<String> = arrayListOf(),
    @JsonProperty("original_language") var originalLanguage: String? = null,
    @JsonProperty("original_name") var originalName: String? = null,
    @JsonProperty("overview") var overview: String? = null,
    @JsonProperty("popularity") var popularity: Double? = null,
    @JsonProperty("poster_path") var posterPath: String? = null,
    @JsonProperty("production_companies") var productionCompanies: ArrayList<ProductionCompanies> = arrayListOf(),
    @JsonProperty("production_countries") var productionCountries: ArrayList<ProductionCountries> = arrayListOf(),
    @JsonProperty("seasons") var seasons: ArrayList<Seasons> = arrayListOf(),
    @JsonProperty("spoken_languages") var spokenLanguages: ArrayList<SpokenLanguages> = arrayListOf(),
    @JsonProperty("status") var status: String? = null,
    @JsonProperty("tagline") var tagline: String? = null,
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("vote_average") var voteAverage: Double? = null,
    @JsonProperty("vote_count") var voteCount: Double? = null
) {
    data class CreatedBy(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("credit_id") var creditId: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("gender") var gender: Int? = null,
        @JsonProperty("profile_path") var profilePath: String? = null
    )

    data class Genres(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null
    )

    data class SpokenLanguages(
        @JsonProperty("english_name") var englishName: String? = null,
        @JsonProperty("iso_639_1") var iso6391: String? = null,
        @JsonProperty("name") var name: String? = null
    )

    data class Seasons(
        @JsonProperty("air_date") var airDate: String? = null,
        @JsonProperty("episode_count") var episodeCount: Int? = null,
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("overview") var overview: String? = null,
        @JsonProperty("poster_path") var posterPath: String? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("vote_average") var voteAverage: Double? = null
    )

    data class ProductionCountries(
        @JsonProperty("iso_3166_1") var iso31661: String? = null,
        @JsonProperty("name") var name: String? = null
    )

    data class ProductionCompanies(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("logo_path") var logoPath: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("origin_country") var originCountry: String? = null
    )

    data class Networks(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("logo_path") var logoPath: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("origin_country") var originCountry: String? = null
    )

    data class Episode(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("overview") var overview: String? = null,
        @JsonProperty("vote_average") var voteAverage: Double? = null,
        @JsonProperty("vote_count") var voteCount: Double? = null,
        @JsonProperty("air_date") var airDate: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("episode_type") var episodeType: String? = null,
        @JsonProperty("production_code") var productionCode: String? = null,
        @JsonProperty("runtime") var runtime: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("show_id") var showId: Int? = null,
        @JsonProperty("still_path") var stillPath: String? = null
    )
}
