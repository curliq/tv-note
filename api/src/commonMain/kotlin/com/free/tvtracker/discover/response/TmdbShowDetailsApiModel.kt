package com.free.tvtracker.discover.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbShowDetailsApiModel(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    @SerialName("status") var status: String,
    @SerialName("backdrop_path") var backdropPath: String? = null,
    @SerialName("episode_run_time") var episodeRunTime: List<Int> = arrayListOf(),
    @SerialName("first_air_date") var firstAirDate: String? = null,
    @SerialName("homepage") var homepage: String? = null,
    @SerialName("in_production") var inProduction: Boolean? = null,
    @SerialName("languages") var languages: List<String> = arrayListOf(),
    @SerialName("last_air_date") var lastAirDate: String? = null,
    @SerialName("last_episode_to_air") var lastEpisodeToAir: Episode? = null,
    @SerialName("next_episode_to_air") var nextEpisodeToAir: Episode? = null,
    @SerialName("networks") var networks: List<Networks> = arrayListOf(),
    @SerialName("number_of_episodes") var numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons") var numberOfSeasons: Int? = null,
    @SerialName("origin_country") var originCountry: List<String> = arrayListOf(),
    @SerialName("original_language") var originalLanguage: String? = null,
    @SerialName("original_name") var originalName: String? = null,
    @SerialName("overview") var overview: String? = null,
    @SerialName("popularity") var popularity: Double? = null,
    @SerialName("poster_path") var posterPath: String? = null,
    @SerialName("production_companies") var productionCompanies: List<ProductionCompanies> = arrayListOf(),
    @SerialName("production_countries") var productionCountries: List<ProductionCountries> = arrayListOf(),
    @SerialName("seasons") var seasons: List<Seasons> = arrayListOf(),
    @SerialName("tagline") var tagline: String? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("vote_average") var voteAverage: Double? = null,
    @SerialName("vote_count") var voteCount: Double? = null
) {

    @Serializable
    data class Seasons(
        @SerialName("air_date") var airDate: String? = null,
        @SerialName("episode_count") var episodeCount: Int? = null,
        @SerialName("id") var id: Int? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("overview") var overview: String? = null,
        @SerialName("poster_path") var posterPath: String? = null,
        @SerialName("season_number") var seasonNumber: Int? = null,
        @SerialName("vote_average") var voteAverage: Double? = null
    )

    @Serializable
    data class ProductionCountries(
        @SerialName("iso_3166_1") var iso31661: String? = null,
        @SerialName("name") var name: String? = null
    )

    @Serializable
    data class ProductionCompanies(
        @SerialName("id") var id: Int? = null,
        @SerialName("logo_path") var logoPath: String? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("origin_country") var originCountry: String? = null
    )

    @Serializable
    data class Networks(
        @SerialName("id") var id: Int,
        @SerialName("name") var name: String,
        @SerialName("logo_path") var logoPath: String? = null,
        @SerialName("origin_country") var originCountry: String? = null
    )

    @Serializable
    data class Episode(
        @SerialName("id") var id: Int? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("overview") var overview: String? = null,
        @SerialName("vote_average") var voteAverage: Double? = null,
        @SerialName("vote_count") var voteCount: Double? = null,
        @SerialName("air_date") var airDate: String? = null,
        @SerialName("episode_number") var episodeNumber: Int? = null,
        @SerialName("episode_type") var episodeType: String? = null,
        @SerialName("production_code") var productionCode: String? = null,
        @SerialName("runtime") var runtime: Int? = null,
        @SerialName("season_number") var seasonNumber: Int? = null,
        @SerialName("show_id") var showId: Int? = null,
        @SerialName("still_path") var stillPath: String? = null
    )
}
