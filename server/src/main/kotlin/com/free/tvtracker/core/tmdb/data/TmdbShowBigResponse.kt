package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel

data class TmdbShowBigResponse(
    @JsonProperty("adult") var adult: Boolean? = null,
    @JsonProperty("backdrop_path") var backdropPath: String? = null,
    @JsonProperty("created_by") var createdBy: List<CreatedBy> = emptyList(),
    @JsonProperty("episode_run_time") var episodeRunTime: List<Int> = emptyList(),
    @JsonProperty("first_air_date") var firstAirDate: String? = null,
    @JsonProperty("genres") var genres: List<Genres> = emptyList(),
    @JsonProperty("homepage") var homepage: String? = null,
    @JsonProperty("id") var id: Int? = null,
    @JsonProperty("in_production") var inProduction: Boolean? = null,
    @JsonProperty("languages") var languages: List<String> = emptyList(),
    @JsonProperty("last_air_date") var lastAirDate: String? = null,
    @JsonProperty("last_episode_to_air") var lastEpisodeToAir: Episode? = Episode(),
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("next_episode_to_air") var nextEpisodeToAir: Episode? = Episode(),
    @JsonProperty("networks") var networks: List<Networks> = emptyList(),
    @JsonProperty("number_of_episodes") var numberOfEpisodes: Int? = null,
    @JsonProperty("number_of_seasons") var numberOfSeasons: Int? = null,
    @JsonProperty("origin_country") var originCountry: List<String> = emptyList(),
    @JsonProperty("original_language") var originalLanguage: String? = null,
    @JsonProperty("original_name") var originalName: String? = null,
    @JsonProperty("overview") var overview: String? = null,
    @JsonProperty("popularity") var popularity: Double? = null,
    @JsonProperty("poster_path") var posterPath: String? = null,
    @JsonProperty("production_companies") var productionCompanies: List<ProductionCompanies> = emptyList(),
    @JsonProperty("production_countries") var productionCountries: List<ProductionCountries> = emptyList(),
    @JsonProperty("seasons") var seasons: List<Seasons> = emptyList(),
    @JsonProperty("spoken_languages") var spokenLanguages: List<SpokenLanguages> = emptyList(),
    @JsonProperty("status") var status: String? = null,
    @JsonProperty("tagline") var tagline: String? = null,
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("vote_average") var voteAverage: Double? = null,
    @JsonProperty("vote_count") var voteCount: Int? = null,
    @JsonProperty("credits") var credits: TmdbShowCreditsResponse? = null,
    @JsonProperty("videos") var videos: TmdbShowVideosResponse? = null,
    @JsonProperty("images") var images: TmdbShowImagesResponse? = null,
    @JsonProperty("watch/providers") var watchProviders: TmdbShowWatchProvidersResponse? = null,
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
    ) {
        fun toApiModel(episodes: List<TmdbShowDetailsApiModel.Season.Episode>) = TmdbShowDetailsApiModel.Season(
            airDate = this.airDate,
            episodeCount = this.episodeCount,
            id = this.id!!,
            name = this.name,
            overview = this.overview,
            posterPath = this.posterPath,
            seasonNumber = this.seasonNumber!!,
            voteAverage = this.voteAverage,
            episodes = episodes
        )
    }

    data class ProductionCountries(
        @JsonProperty("iso_3166_1") var iso31661: String? = null,
        @JsonProperty("name") var name: String? = null
    ) {
        fun toApiModel() = TmdbShowDetailsApiModel.ProductionCountries(
            name = this.name,
        )
    }

    data class ProductionCompanies(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("logo_path") var logoPath: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("origin_country") var originCountry: String? = null
    ) {
        fun toApiModel() = TmdbShowDetailsApiModel.ProductionCompanies(
            id = this.id,
            logoPath = this.logoPath,
            name = this.name,
            originCountry = this.originCountry,
        )
    }

    data class Networks(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("logo_path") var logoPath: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("origin_country") var originCountry: String? = null
    ) {
        fun toApiModel() = TmdbShowDetailsApiModel.Networks(
            id = id!!,
            name = name!!,
            logoPath = logoPath,
            originCountry = originCountry,
        )
    }

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
    ) {
        fun toApiModel() = TmdbShowDetailsApiModel.Episode(
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
    }
}
