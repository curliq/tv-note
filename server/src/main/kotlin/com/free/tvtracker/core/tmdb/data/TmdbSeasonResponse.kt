package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

class TmdbSeasonResponse(
    @JsonProperty("_id") var pseudoId: String? = null,
    @JsonProperty("air_date") var airDate: String? = null,
    @JsonProperty("episodes") var episodes: List<Episodes>? = null,
    @JsonProperty("name") var name: String? = null,
    @JsonProperty("overview") var overview: String? = null,
    @JsonProperty("id") var id: Int? = null,
    @JsonProperty("poster_path") var posterPath: String? = null,
    @JsonProperty("season_number") var seasonNumber: Int? = null,
    @JsonProperty("vote_average") var voteAverage: Double? = null
) {
    data class Episodes(
        @JsonProperty("air_date") var airDate: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("episode_type") var episodeType: String? = null,
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("overview") var overview: String? = null,
        @JsonProperty("production_code") var productionCode: String? = null,
        @JsonProperty("runtime") var runtime: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("show_id") var showId: Int? = null,
        @JsonProperty("still_path") var stillPath: String? = null,
        @JsonProperty("vote_average") var voteAverage: Double? = null,
        @JsonProperty("vote_count") var voteCount: Int? = null,
        @JsonProperty("crew") var crew: List<Crew>? = null,
        @JsonProperty("guest_stars") var guestStars: List<GuestStar>? = null
    ) {

        data class Crew(
            @JsonProperty("job") val job: String? = null,
            @JsonProperty("department") val department: String? = null,
            @JsonProperty("credit_id") val creditId: String? = null,
            @JsonProperty("adult") val adult: Boolean = false,
            @JsonProperty("gender") val gender: Int? = null,
            @JsonProperty("id") val id: Int? = null,
            @JsonProperty("known_for_department") val knownForDepartment: String? = null,
            @JsonProperty("name") val name: String? = null,
            @JsonProperty("original_name") val originalName: String? = null,
            @JsonProperty("popularity") val popularity: Double?,
            @JsonProperty("profile_path") val profilePath: String? = null,
        )


        data class GuestStar(
            @JsonProperty("credit_id") val creditId: String? = null,
            @JsonProperty("character") val character: String? = null,
            @JsonProperty("adult") val adult: Boolean = false,
            @JsonProperty("gender") val gender: Int? = null,
            @JsonProperty("id") val id: Int? = null,
            @JsonProperty("order") val order: Int? = null,
            @JsonProperty("known_for_department") val knownForDepartment: String? = null,
            @JsonProperty("name") val name: String? = null,
            @JsonProperty("original_name") val originalName: String? = null,
            @JsonProperty("popularity") val popularity: Double?,
            @JsonProperty("profile_path") val profilePath: String? = null,
        )
    }
}
