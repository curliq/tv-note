package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.discover.response.TmdbShowDetailsApiModel


data class TmdbShowCreditsResponse(
    @JsonProperty("cast") var cast: List<Cast> = emptyList(),
    @JsonProperty("crew") var crew: List<Crew> = emptyList()
) {
    data class Crew(
        @JsonProperty("adult") var adult: Boolean? = null,
        @JsonProperty("gender") var gender: Int? = null,
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("known_for_department") var knownForDepartment: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("original_name") var originalName: String? = null,
        @JsonProperty("popularity") var popularity: Double? = null,
        @JsonProperty("profile_path") var profilePath: String? = null,
        @JsonProperty("credit_id") var creditId: String? = null,
        @JsonProperty("department") var department: String? = null,
        @JsonProperty("job") var job: String? = null
    ) {
        fun toApiModel(): TmdbShowDetailsApiModel.Crew {
            return TmdbShowDetailsApiModel.Crew(
                gender = this.gender,
                id = this.id,
                knownForDepartment = this.knownForDepartment,
                name = this.name,
                originalName = this.originalName,
                popularity = this.popularity,
                profilePath = this.profilePath,
                creditId = this.creditId,
                department = this.department,
                job = this.job,
            )
        }
    }

    data class Cast(
        @JsonProperty("adult") var adult: Boolean? = null,
        @JsonProperty("gender") var gender: Int? = null,
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("known_for_department") var knownForDepartment: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("original_name") var originalName: String? = null,
        @JsonProperty("popularity") var popularity: Double? = null,
        @JsonProperty("profile_path") var profilePath: String? = null,
        @JsonProperty("character") var character: String? = null,
        @JsonProperty("credit_id") var creditId: String? = null,
        @JsonProperty("order") var order: Int? = null
    ) {
        fun toApiModel(): TmdbShowDetailsApiModel.Cast {
            return TmdbShowDetailsApiModel.Cast(
                gender = this.gender,
                id = this.id,
                knownForDepartment = this.knownForDepartment,
                name = this.name,
                originalName = this.originalName,
                popularity = this.popularity,
                profilePath = this.profilePath,
                character = this.character,
                creditId = this.creditId,
                order = this.order,
            )
        }
    }
}
