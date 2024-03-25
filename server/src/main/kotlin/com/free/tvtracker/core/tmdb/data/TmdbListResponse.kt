package com.free.tvtracker.core.tmdb.data

import com.fasterxml.jackson.annotation.JsonProperty

open class TmdbListResponse<T>(
    @JsonProperty("page") var page: Int? = null,
    @JsonProperty("results") var results: ArrayList<T> = arrayListOf(),
    @JsonProperty("total_pages") var totalPages: Int? = null,
    @JsonProperty("total_results") var totalResults: Int? = null
)
