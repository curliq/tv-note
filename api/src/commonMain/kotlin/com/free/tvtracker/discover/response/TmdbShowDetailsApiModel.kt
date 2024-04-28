package com.free.tvtracker.discover.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TmdbShowDetailsApiModel(
    @SerialName("id") var id: Int,
    @SerialName("name") var name: String,
    @SerialName("status") var status: String,
    @SerialName("backdrop_path") var backdropPath: String? = null,
    @SerialName("episode_run_time") var episodeRunTime: List<Int> = emptyList(),
    @SerialName("first_air_date") var firstAirDate: String? = null,
    @SerialName("homepage") var homepage: String? = null,
    @SerialName("in_production") var inProduction: Boolean? = null,
    @SerialName("languages") var languages: List<String> = emptyList(),
    @SerialName("last_air_date") var lastAirDate: String? = null,
    @SerialName("last_episode_to_air") var lastEpisodeToAir: Episode? = null,
    @SerialName("next_episode_to_air") var nextEpisodeToAir: Episode? = null,
    @SerialName("networks") var networks: List<Networks> = emptyList(),
    @SerialName("number_of_episodes") var numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons") var numberOfSeasons: Int? = null,
    @SerialName("origin_country") var originCountry: List<String> = emptyList(),
    @SerialName("original_language") var originalLanguage: String? = null,
    @SerialName("original_name") var originalName: String? = null,
    @SerialName("overview") var overview: String? = null,
    @SerialName("popularity") var popularity: Double? = null,
    @SerialName("poster_path") var posterPath: String? = null,
    @SerialName("production_companies") var productionCompanies: List<ProductionCompanies> = emptyList(),
    @SerialName("production_countries") var productionCountries: List<ProductionCountries> = emptyList(),
    @SerialName("seasons") var seasons: List<Season> = emptyList(),
    @SerialName("tagline") var tagline: String? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("vote_average") var voteAverage: Double? = null,
    @SerialName("vote_count") var voteCount: Int? = null,
    @SerialName("videos") var videos: List<Video>? = null,
    @SerialName("images") var images: Images? = null,
    @SerialName("cast") var cast: List<Cast>? = null,
    @SerialName("crew") var crew: List<Crew>? = null,
    @SerialName("watchProvider") var watchProvider: List<WatchProvider>? = null,
) {
    @Serializable
    data class Season(
        @SerialName("id") var id: Int,
        @SerialName("season_number") var seasonNumber: Int,
        @SerialName("air_date") var airDate: String? = null,
        @SerialName("episode_count") var episodeCount: Int? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("overview") var overview: String? = null,
        @SerialName("poster_path") var posterPath: String? = null,
        @SerialName("vote_average") var voteAverage: Double? = null,
        @SerialName("episodes") var episodes: List<Episode>? = null,
    ) {
        @Serializable
        data class Episode(
            @SerialName("id") var id: Int,
            @SerialName("number") var number: Int,
            @SerialName("name") var name: String? = null,
            @SerialName("air_date") var airDate: String? = null,
            @SerialName("thumbnail") var thumbnail: String? = null,
            @Transient var seasonNumber: Int? = null,
        )
    }

    @Serializable
    data class ProductionCountries(
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

    @Serializable
    data class Crew(
        @SerialName("gender") var gender: Int? = null,
        @SerialName("id") var id: Int? = null,
        @SerialName("known_for_department") var knownForDepartment: String? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("original_name") var originalName: String? = null,
        @SerialName("popularity") var popularity: Double? = null,
        @SerialName("profile_path") var profilePath: String? = null,
        @SerialName("credit_id") var creditId: String? = null,
        @SerialName("department") var department: String? = null,
        @SerialName("job") var job: String? = null
    )

    @Serializable
    data class Cast(
        @SerialName("gender") var gender: Int? = null,
        @SerialName("id") var id: Int? = null,
        @SerialName("known_for_department") var knownForDepartment: String? = null,
        @SerialName("name") var name: String? = null,
        @SerialName("original_name") var originalName: String? = null,
        @SerialName("popularity") var popularity: Double? = null,
        @SerialName("profile_path") var profilePath: String? = null,
        @SerialName("character") var character: String? = null,
        @SerialName("credit_id") var creditId: String? = null,
        @SerialName("order") var order: Int? = null
    )

    @Serializable
    data class Images(
        @SerialName("backdrops") var backdrops: List<Backdrops> = emptyList(),
        @SerialName("logos") var logos: List<Logos> = emptyList(),
        @SerialName("posters") var posters: List<Posters> = emptyList()
    ) {
        @Serializable
        data class Backdrops(
            @SerialName("aspect_ratio") var aspectRatio: Double? = null,
            @SerialName("height") var height: Int? = null,
            @SerialName("file_path") var filePath: String? = null,
            @SerialName("vote_average") var voteAverage: Double? = null,
            @SerialName("vote_count") var voteCount: Int? = null,
            @SerialName("width") var width: Int? = null
        )

        @Serializable
        data class Logos(
            @SerialName("aspect_ratio") var aspectRatio: Double? = null,
            @SerialName("height") var height: Int? = null,
            @SerialName("file_path") var filePath: String? = null,
            @SerialName("vote_average") var voteAverage: Double? = null,
            @SerialName("vote_count") var voteCount: Int? = null,
            @SerialName("width") var width: Int? = null
        )

        @Serializable
        data class Posters(
            @SerialName("aspect_ratio") var aspectRatio: Double? = null,
            @SerialName("height") var height: Int? = null,
            @SerialName("file_path") var filePath: String? = null,
            @SerialName("vote_average") var voteAverage: Double? = null,
            @SerialName("vote_count") var voteCount: Int? = null,
            @SerialName("width") var width: Int? = null
        )
    }

    @Serializable
    data class Video(
        @SerialName("name") var name: String? = null,
        @SerialName("key") var key: String? = null,
        @SerialName("site") var site: String? = null,
        @SerialName("size") var size: Int? = null,
        @SerialName("type") var type: String? = null,
        @SerialName("official") var official: Boolean? = null,
        @SerialName("published_at") var publishedAt: String? = null,
        @SerialName("id") var id: String? = null
    )

    @Serializable
    data class WatchProvider(
        @SerialName("logo_path") var logoPath: String? = null,
        @SerialName("provider_id") var providerId: Int? = null,
        @SerialName("provider_name") var providerName: String? = null,
        @SerialName("display_priority") var displayPriority: Int? = null
    )
}
