package com.free.tvtracker.search

import com.fasterxml.jackson.annotation.JsonProperty
import com.free.tvtracker.core.tmdb.data.TmdbSearchMultiResponse

data class SearchPersonApiModel(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("popularity") val popularity: Double? = null,
    @JsonProperty("origin_country") val originCountry: ArrayList<String>?,
    @JsonProperty("gender") val gender: Int,
    @JsonProperty("known_for_department") val knownForDepartment: String?,
)

fun TmdbSearchMultiResponse.Data.toPersonApiModel(): SearchPersonApiModel {
    return SearchPersonApiModel(
        id = id!!,
        name = name!!,
        popularity = popularity,
        originCountry = originCountry,
        gender = gender!!,
        knownForDepartment = knownForDepartment,
    )
}
