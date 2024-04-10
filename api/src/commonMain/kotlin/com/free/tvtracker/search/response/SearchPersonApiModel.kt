package com.free.tvtracker.search.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPersonApiModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("gender") val gender: Int? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("origin_country") val originCountry: List<String>? = null,
)
