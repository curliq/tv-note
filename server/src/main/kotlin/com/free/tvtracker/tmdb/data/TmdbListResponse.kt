package com.free.tvtracker.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

open class TmdbListResponse<T>(
    @get:JsonProperty("page") val page: Int? = null,
    @get:JsonProperty("results") var results: ArrayList<T> = arrayListOf(),
    @get:JsonProperty("total_pages") open val totalPages: Int? = null,
    @get:JsonProperty("total_results") val totalResults: Int? = null
)
